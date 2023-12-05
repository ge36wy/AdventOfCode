import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    static ArrayList<Range> next = new ArrayList<>();
    public static void main(String[] args) {
        InputStream input = Main.class.getResourceAsStream("input.txt");
        try {
            assert input != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            String[] seeds = lines.get(0).split(": ")[1].split("\s");
            ArrayList<Long> seedNum = new ArrayList<>();
            for (String seed : seeds) {
                seedNum.add(Long.parseLong(seed));
            }
            ArrayList<Range> ranges = new ArrayList<>();
            for(int i = 0; i < seedNum.size(); i+=2){
                ranges.add(new Range(seedNum.get(i), seedNum.get(i) + seedNum.get(i + 1) - 1));
                //System.out.println(seedNum.get(i) + ", " + (seedNum.get(i) + seedNum.get(i + 1) - 1));
            }

            ArrayList<Range> areas = new ArrayList<>();
            areas.add(new Range(3, 20));
            areas.add(new Range(23, 30));
            areas.add(new Range(33, 67));
            areas.add(new Range(70, 114));
            areas.add(new Range(117, 130));
            areas.add(new Range(133, 160));
            areas.add(new Range(163, 173));

            for(Range r: areas) {
                ArrayList<String> relevantLines = new ArrayList<>();
                for(long i = r.start; i <= r.finish; i++){
                    relevantLines.add(lines.get((int)i));
                }
                ArrayList<mapp> relevantMap = new ArrayList<>();
                for(String s: relevantLines){
                    String[] sp = s.split("\s");
                    relevantMap.add(new mapp(Long.parseLong(sp[0]), Long.parseLong(sp[1]), Long.parseLong(sp[2])));
                }
                relevantMap.sort(Comparator.comparingLong(z -> z.source));
                for (Range current : ranges) {
                    addNext(relevantMap, current);
                }
                ranges = next;
                next = new ArrayList<>();
            }
            /*for(Range r: areas){
                ArrayList<String> relevantLines = new ArrayList<>();
                for(long i = r.start; i <= r.finish; i++){
                    relevantLines.add(lines.get((int)i));
                }
                ArrayList<mapp> relevantMap = new ArrayList<>();
                for(String s: relevantLines){
                    String[] sp = s.split("\s");
                    relevantMap.add(new mapp(Long.parseLong(sp[0]), Long.parseLong(sp[1]), Long.parseLong(sp[2])));
                }
                for(int i = 0; i < seedNum.size(); i++) {
                    for (mapp m : relevantMap) {
                        if(seedNum.get(i) >= m.source && seedNum.get(i) < m.source + m.length){
                            seedNum.set(i, (seedNum.get(i) - m.source) + m.destination);
                            break;
                        }
                    }
                }
            }*/
            long min = Long.MAX_VALUE;
            for(Range range: ranges) {
                if(range.start < min) min = range.start;
            }
            System.out.println(min);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNext(ArrayList<mapp> relevantMap, Range current){
        int j = 0;
        while(j < relevantMap.size()){
            if(current.start < relevantMap.get(j).source + relevantMap.get(j).length){
                break;
            }
            j++;
        }
        long sourceStart = relevantMap.get(j).source;
        long length = relevantMap.get(j).length;

        if(current.start < sourceStart){
            if(current.finish < sourceStart){
                next.add(current);
            }else{
                next.add(new Range(current.start, sourceStart - 1));
                addNext(relevantMap, new Range(sourceStart, current.finish));
            }
        }else{
            if(current.finish < (sourceStart + length)){
                next.add(new Range((current.start - sourceStart) + relevantMap.get(j).destination, (current.finish - sourceStart) + relevantMap.get(j).destination));
            }else{
                next.add(new Range((current.start - sourceStart) + relevantMap.get(j).destination, (relevantMap.get(j).destination + length) - 1));
                addNext(relevantMap, new Range(relevantMap.get(j).source + length, current.finish));
            }
        }
    }
}