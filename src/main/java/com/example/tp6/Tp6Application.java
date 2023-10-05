package com.example.tp6;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Tp6Application {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    static Random rand = new Random();
    static double hv = 9999999;

    public static void main(String[] args) {
        List<Integer> capacidadesAscensores = List.of(3, 4, 8, 12, 20);
        List<Integer> cantidadesAscensores = List.of(1, 2, 3, 4, 5, 6);
        List<String> colors = List.of(ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE);
        int indiceColor = 0;
        for (double CA : capacidadesAscensores) {
            for (double NA : cantidadesAscensores) {
                double t = 0, fin = hv;
                double ns = 0, tpll = 0, cantLlegadas = 0;
                double sumTiempoLLegada = 0, sumTiempoSalida = 0;
                double[] tps = new double[(int) NA];

                Arrays.fill(tps, hv);

                while (t < fin) {
                    int indiceProxTps = getProximoTps(tps);
                    double proxTps = tps[indiceProxTps];

                    if (proxTps < tpll) {
                        //subida
                        t = proxTps;
                        if (ns > 0) {
                            //se suben al ascensor
                            actualizarTiempoProximaSubida(indiceProxTps, tps, ns, CA, t); // se actualiza el tps_i
                            double cantidadDeSubidas = Math.min(ns, CA);
                            sumTiempoSalida += cantidadDeSubidas * t;
                            ns -= cantidadDeSubidas;
                        } else {
                            tps[indiceProxTps] = hv;
                        }

                    } else {
                        //llegada
                        t = tpll;
                        tpll += calcularIA();
                        cantLlegadas++;
                        ns++;

                        if (proxTps < t || proxTps == hv) {
                            //el ascensor esta en planta baja
                            actualizarTiempoProximaSubida(indiceProxTps, tps, ns, CA, t); // se actualiza el tps_i
                            ns--;
                        } else {
                            //la persona espera
                            sumTiempoLLegada += t;
                        }
                    }
                }

                while (ns != 0) {
                    int indiceProxTps = getProximoTps(tps);
                    double proxTps = tps[indiceProxTps];
                    t = proxTps;
                    if (ns > 0) {
                        //se suben al ascensor
                        actualizarTiempoProximaSubida(indiceProxTps, tps, ns, CA, t); // se actualiza el tps_i
                        double cantidadDeSubidas = Math.min(ns, CA);
                        sumTiempoSalida += cantidadDeSubidas * t;
                        ns -= cantidadDeSubidas;
                    }
                }
                System.out.printf(colors.get(indiceColor) + "Cantidad de ascensores: %s - Capacidad: %s - ", NA, CA);
                System.out.printf(colors.get(indiceColor) + "Tiempo promedio de espera: %s%n", Math.floor((sumTiempoSalida - sumTiempoLLegada) / cantLlegadas));
            }
            indiceColor++;
        }
    }

    private static void actualizarTiempoProximaSubida(int indiceProxTps, double[] tps, double ns, double ca, double t) {
        double tp = tps[indiceProxTps];
        tps[indiceProxTps] = t + rand.nextInt(30) + 1;
    }

    private static int getProximoTps(double[] tps) {
        double valorMinimo = tps[0];
        int posicionMinima = 0;

        for (int i = 1; i < tps.length; i++) {
            if (tps[i] == hv) {
                return i;
            }
            if (tps[i] < valorMinimo) {
                valorMinimo = tps[i];
                posicionMinima = i;
            }
        }

        return posicionMinima;
    }

    private static double calcularIA() {
        return rand.nextInt(2) + 1;
    }
}
