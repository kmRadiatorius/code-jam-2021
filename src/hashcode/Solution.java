package hashcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Solution {

    static class Street {
        int B;
        int E;
        String street;
        int L;
        Queue<Car> carQueue = new LinkedList<>();
        int totalCars = 0;
    }

    static class Car {
        int P;
        int currentStreet = 0;
        String[] streets;
    }

    static class TrafficLight {
        String name;
        int seconds;
    }

    static class Intersection {
        int nr;
        List<TrafficLight> trafficLights = new ArrayList<>();
        List<Street> streetsBegin = new ArrayList<>();
        List<Street> streetsEnd = new ArrayList<>();
    }

    static String fileName = "f.txt";

    static int D, I, S, V, F;
    static Street[] streets = new Street[100000];
    static Car[] cars = new Car[1000];
    static Intersection[] intersections = new Intersection[100000];
    static Map<String, Street> streetsMap = new HashMap<>();

    public static void main(String[] args) {
        readData();

        for (int i = 0; i < S; i++) {
            streetsMap.put(streets[i].street, streets[i]);
        }

        for (int i = 0; i < V; i++) {
            streetsMap.get(cars[i].streets[0]).carQueue.add(cars[i]);
            for (int j = 0; j < cars[i].P; j++) {
                streetsMap.get(cars[i].streets[j]).totalCars++;
            }
        }


        for (int i = 0; i < I; i++) {
            Intersection intersection = intersections[i];

            intersection.streetsEnd.sort((a, b) -> a.totalCars - b.totalCars);

            int min = 999999;
            for (Street str : intersection.streetsEnd) {
                if (str.totalCars < min && str.totalCars > 0) {
                    min = str.totalCars;
                }
            }

            int j = 0;
            for (Street str : intersection.streetsEnd) {
                if (str.totalCars > 0) {
                    TrafficLight tl = new TrafficLight();
                    tl.name = str.street;
                    tl.seconds = Math.floorDiv(str.totalCars, min);
                    if (tl.seconds > 30) {
                        tl.seconds = 30;
                    }
                    intersection.trafficLights.add(tl);
                }
            }
        }

        writeData();

    }

    static int gcd(int a, int b) {
        if (a == 0)
            return b;
        return gcd(b % a, a);
    }

    static void readData() {
        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);

            String[] data = reader.nextLine().split(" ");
            D = Integer.parseInt(data[0]);
            I = Integer.parseInt(data[1]);
            S = Integer.parseInt(data[2]);
            V = Integer.parseInt(data[3]);
            F = Integer.parseInt(data[4]);

            for (int i = 0; i < I; i++) {
                intersections[i] = new Intersection();
            }

            for (int i = 0; i < S; i++) {
                data = reader.nextLine().split(" ");
                Street s = new Street();
                s.B = Integer.parseInt(data[0]);
                s.E = Integer.parseInt(data[1]);
                s.street = data[2];
                s.L = Integer.parseInt(data[3]);
                streets[i] = s;
                intersections[s.B].streetsBegin.add(s);
                intersections[s.B].nr = s.B;
                intersections[s.E].streetsEnd.add(s);
                intersections[s.E].nr = s.E;
            }

            for (int i = 0; i < V; i++) {
                data = reader.nextLine().split(" ");
                Car p = new Car();
                p.P = Integer.parseInt(data[0]);
                p.streets = new String[p.P];
                for (int j = 0; j < p.P; j++) {
                    p.streets[j] = data[j + 1];
                }
                cars[i] = p;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static void writeData() {

//        List<Intersection> intersections = Arrays.stream(Solution1.intersections)
//                .filter(a -> !a.trafficLights.isEmpty())
//                .collect(Collectors.toList());

        int tlCount = 0;
        for (int i = 0; i < I; i++) {
            if (intersections[i].trafficLights.size() > 0) {
                tlCount++;
            }
        }

        try {
            FileWriter writer = new FileWriter("solution_" + fileName);
            writer.write(tlCount + "\n");
            for (int i = 0; i < I; i++) {
                Intersection intersection = intersections[i];
                if (intersection.trafficLights.size() > 0) {
                    writer.write(intersection.nr + "\n");
                    writer.write(intersection.trafficLights.size() + "\n");
                    for (TrafficLight trafficLight : intersection.trafficLights) {
                        writer.write(trafficLight.name + " " + trafficLight.seconds + "\n");
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
