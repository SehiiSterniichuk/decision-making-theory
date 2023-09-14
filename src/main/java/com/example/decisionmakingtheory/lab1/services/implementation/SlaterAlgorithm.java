package com.example.decisionmakingtheory.lab1.services.implementation;

public class SlaterAlgorithm extends AbstractAlgorithm {//Алгоритм Слейтера
    @Override
    protected boolean checkCondition(int[] x1, int[] x2) {//Перевіряємо f(Xi)>Sf(Xj)
        for (int i = 0; i < x1.length; i++) {
            if (x2[i] >= x1[i]) {
                //якщо хоча б один критерій з альтернативи
                // Xj(x2) більше АБО рівний за Xi(x1),
                // то ми не виключаємо цю альтернативу
                // Тобто Слейтер виключає альтернативи,
                // які є строго домінованими за УСІМА категоріями
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "Slater";
    }
}
