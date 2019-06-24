package com.hprof.bitmap;

import com.squareup.haha.perflib.ArrayInstance;
import com.squareup.haha.perflib.ClassInstance;
import com.squareup.haha.perflib.ClassObj;
import com.squareup.haha.perflib.Heap;
import com.squareup.haha.perflib.HprofParser;
import com.squareup.haha.perflib.Instance;
import com.squareup.haha.perflib.Snapshot;
import com.squareup.haha.perflib.io.MemoryMappedFileBuffer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.hprof.bitmap.Utils.getTraceFromInstance;

public class Main {

    public static void main(String[] args) throws IOException {
        // 打开hprof文件
        File hprofFile = new File("hprof/memory-20190624T155221.hprof");
        if (!hprofFile.exists() || !hprofFile.isFile()) {
            return;
        }
        //分析结果
        Map<String, List<BitmapNode>> result = new HashMap<>();

        //解析文件
        MemoryMappedFileBuffer memoryMappedFileBuffer = new MemoryMappedFileBuffer(hprofFile);
        HprofParser parser = new HprofParser(memoryMappedFileBuffer);
        // 获得snapshot(内存快照)
        Snapshot snapshot = parser.parse();
        snapshot.computeDominators();
        // 获得Bitmap Class
        final ClassObj bitmapClass = snapshot.findClass("android.graphics.Bitmap");
        // 获得 heap, 只需要分析 app 和 default heap 即可
        Collection<Heap> heaps = snapshot.getHeaps();
        heaps.forEach(heap -> {
            String name = heap.getName();
            System.out.println("heap file -> " + name);
            if ("default".equals(name) || "app".equals(name)) {
                // 从 heap 中获得所有的 Bitmap 实例
                final List<Instance> bitmapInstances = bitmapClass.getHeapInstances(heap.getId());
                bitmapInstances.forEach(bitmapInstance -> {
                    analyzeBuffer(result, bitmapInstance);
                });
            }
        });

        filterNormalSize(result);

        outputResult(result);
    }

    private static void outputResult(Map<String, List<BitmapNode>> result) {
        result.forEach((key, instances) -> {
            System.out.println("-------------------------------------------------");
            System.out.println("key = " + key);
            Instance instance;
            for (int i = 0; i < instances.size(); i++) {
                instance = instances.get(i).instance;
                System.out.println("instance：" + i);
                System.out.println("heap: " + instance.getHeap().getName());
                int width = HahaHelper.fieldValue(HahaHelper.classInstanceValues(instance), "mWidth");
                int height = HahaHelper.fieldValue(HahaHelper.classInstanceValues(instance), "mHeight");
                System.out.println(String.format("width = %d height = %d", width, height));
                for (Instance node : getTraceFromInstance(instance)) {
                    System.out.println(node);
                }
                System.out.println();
            }
            System.out.println();
        });
    }

    private static void filterNormalSize(Map<String, List<BitmapNode>> result) {
        Set<String> keys = new HashSet<>();
        result.forEach((key, instances) -> {
            if (instances.size() <= 1) {
                keys.add(key);
            }
        });
        keys.forEach(result::remove);
    }


    private static void analyzeBuffer(Map<String, List<BitmapNode>> result, Instance bitmapInstance) {
        // 从 Bitmap 实例中获得 buffer 数组
        ArrayInstance buffer = HahaHelper.fieldValue(HahaHelper.classInstanceValues(bitmapInstance), "mBuffer");

        byte[] byteArray = Utils.getByteArray(buffer);
        String hashCode = String.valueOf(Arrays.hashCode(byteArray));
        if (result.containsKey(hashCode)) {
            result.get(hashCode).add(new BitmapNode(bitmapInstance, buffer));
        } else {
            ArrayList<BitmapNode> instants = new ArrayList<>();
            instants.add(new BitmapNode(bitmapInstance, buffer));
            result.put(hashCode, instants);
        }
    }

}

class BitmapNode {
    final Instance instance;
    final ArrayInstance buffer;

    BitmapNode(Instance instance, ArrayInstance buffer) {
        this.instance = instance;
        this.buffer = buffer;
    }
}
