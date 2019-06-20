package sample;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class DataFilterSample {

    private static class Data {

        public final int id;
        public final String remote_id;
        public final String date;

        @Override
        public String toString() {
            return "Data{" +
                    "id=" + id +
                    ", remote_id='" + remote_id + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }

        private Data(int id, String remote_id, String date) {
            this.id = id;
            this.remote_id = remote_id;
            this.date = date;

        }
    }


    private static final List<Data> dataList = new ArrayList<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String currentTime(int i) {
        return sdf.format(new Date((System.currentTimeMillis() + i * 1000)));
    }

    public static void main(String... args) {


        initData();
        Observable.fromIterable(dataList)
                .filter(data -> {
                    if (data.remote_id == null) {
                        System.out.println("insert null data--" + data.id);
                    }
                    return data.remote_id != null;
                })
                .groupBy(data -> data.remote_id)
                .flatMap(stringDataGroupedObservable -> stringDataGroupedObservable.sorted(Comparator.comparing(o2 -> o2.date)).takeLast(1))
                .subscribe(System.out::println);
        System.out.println("------------------------------------------------------------");
        Observable.fromIterable(dataList)
                .sorted(Comparator.comparing(o -> o.date))
                .subscribe(System.out::println);
    }

    private static void initData() {
        int mod;
        Data data;
        for (int i = 0; i < 100; i++) {
            mod = i % 3;
            if (mod == 0) {
                data = new Data(i, null, currentTime(i));
            } else if (mod == 1) {
                data = new Data(i, "0002", currentTime(i));
            } else {
                data = new Data(i, "0001", currentTime(i));
            }
            dataList.add(data);
        }
    }
}
