package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.implementation;

public class ParetoAlgorithm extends AbstractAlgorithm {//Алгоритм Парето
    @Override
    protected boolean checkCondition(int[] x1, int[] x2) {//Перевіряємо f(Xi)>Pf(Xj)
        for (int i = 0; i < x1.length; i++) {
            if (x2[i] > x1[i]) {
                //якщо хоча б один критерій з альтернативи
                // Xj(x2) більше за Xi(x1), то ми не виключаємо цю альтернативу
                // Тобто Парето виключає альтернативи,
                // які є домінованими АБО рівними хоча б за одним критерієм
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "Pareto";
    }
}
