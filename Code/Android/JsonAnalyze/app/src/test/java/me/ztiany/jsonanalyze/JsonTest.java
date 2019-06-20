package me.ztiany.jsonanalyze;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.ztiany.jsonanalyze.model.SampleEntity;
import me.ztiany.jsonanalyze.utils.JsonUtils;
import okio.Okio;

/**
 * @author Ztiany
 * Date : 2018-08-13 12:21
 */
public class JsonTest {


    @Test
    public void testBuildSampleEntity() throws IOException {
        final int size = 3;
        List<SampleEntity> sampleEntityList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            sampleEntityList.add(SampleEntity.random());
        }
        File file = new File(size + "_json.json");
        String json = JsonUtils.toJson(sampleEntityList);
        Okio.buffer(Okio.sink(file)).writeUtf8(json).flush();
    }

}
