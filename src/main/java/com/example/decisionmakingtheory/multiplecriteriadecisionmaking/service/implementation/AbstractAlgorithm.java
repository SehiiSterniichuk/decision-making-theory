package com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.implementation;

import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.AlternativeCriteriaTable;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.domain.Domination;
import com.example.decisionmakingtheory.multiplecriteriadecisionmaking.service.Algorithm;

import java.util.Arrays;

public abstract class AbstractAlgorithm implements Algorithm {
    @Override
    public final Domination apply(AlternativeCriteriaTable alternativeCriteriaTable) {
        int[][] alternatives = alternativeCriteriaTable.alternatives();//Отримана таблиця альтернатив зчитана з файлу
        for (int i = 1; i < alternatives.length; i++) {
            assert alternatives[i - 1].length == alternatives[i].length;//валідація
        }
        int[] domination = new int[alternatives.length];//створюємо масив рішень
        Arrays.fill(domination, -1);//Заповнюємо масив. -1 означає що рішення ПОКИ ЩО підходить
        final int N = alternatives.length;
        for (int i = 0, j = 1; ; ) {
            if (domination[j] == -1 && domination[i] == -1 && //Якщо обидві альтернативи досі доступні
                    !Arrays.equals(alternatives[j], alternatives[i])) {//також якщо альтернативи різні
                if (checkCondition(alternatives[i], alternatives[j])) {//то ми перевіряємо умову за нерівністю Парето або Слейтера
                    domination[j] = i;//f(Xi)>Pf(Xj) OR f(Xi)>Sf(Xj)
                } else if (checkCondition(alternatives[j], alternatives[i])) {
                    domination[i] = j;//f(Xj)>Pf(Xi) OR f(Xj)>Sf(Xi)
                }
            }
            if (j < N - 1) {
                j++;//пересуваємо вказівник другої альтернативи
            } else if (i < N - 2) {
                i++;//пересуваємо вказівник першої альтернативи
                j = i + 1;//а вказівник другої робимо наступним
            } else {
                break;//закінчуємо алгоритм при перевірці всіх альтернатив
            }
        }
        return new Domination(domination);
    }

    protected abstract boolean checkCondition(int[] x1, int[] x2);//Класи нащадки імплементують метод нерівності. Читати 'Template Method' патерн.
    //Use the Template Method pattern when you want
    // to let clients extend only particular steps of an algorithm,
    // but not the whole algorithm or its structure.
}
