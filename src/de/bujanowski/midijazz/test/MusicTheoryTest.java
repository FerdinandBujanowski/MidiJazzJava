package de.bujanowski.midijazz.test;

import de.bujanowski.midijazz.theory.music.MusicTheory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MusicTheoryTest {

    @Test
    void isBlackKey() {
        int[] blackKeys = {1, 3, 6, 8, 10};
        for(int blackKey : blackKeys) {
            Assertions.assertTrue(MusicTheory.isBlackKey(blackKey));
        }
        int[] whiteKeys = {0, 2, 4, 5, 7, 9, 11};
        for(int whiteKey : whiteKeys) {
            Assertions.assertFalse(MusicTheory.isBlackKey(whiteKey));
        }
    }

    @Test
    void getRootName() {
        Assertions.assertEquals(MusicTheory.getRootName(60), "C");
        Assertions.assertEquals(MusicTheory.getRootName(66), "(F#/Gb)");
    }

    @Test
    void translateCircleToChromatic() {
        Assertions.assertEquals(MusicTheory.getRootName(MusicTheory.translateCircleToChromatic(-1)), "F");
        Assertions.assertEquals(MusicTheory.getRootName(MusicTheory.translateCircleToChromatic(11)), "F");
        Assertions.assertEquals(MusicTheory.getRootName(MusicTheory.translateCircleToChromatic(1)), "G");
        Assertions.assertEquals(MusicTheory.getRootName(MusicTheory.translateCircleToChromatic(-3)), "Eb");
    }
}