package de.bujanowski.midijazz.theory.reader;

import com.google.gson.*;
import de.bujanowski.midijazz.theory.music.MusicTheory;
import de.bujanowski.midijazz.theory.music.elements.Note;
import de.bujanowski.midijazz.theory.music.elements.chords.ChordFamily;
import de.bujanowski.midijazz.theory.music.elements.chords.IVoicing;
import de.bujanowski.midijazz.theory.music.elements.chords.Voicing;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Pipeline {

    public static final String basePath = "src/de/bujanowski/midijazz/resources/pipelines/";

    public static final String TITLE = "title";
    public static final String DESC = "description";
    public static final String SKILLS = "skills";

    // skill
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String VALUE = "value";
    public static final String HAS_ROOT = "has_root";
    public static final String C_OFFSET = "circle_offset";
    public static final String C_PROGRESS = "circle_progress";

    // types
    public static final String CHORD = "chord";

    // chord object
    public static final String FAMILY = "family";
    public static final String CHILD = "child";
    public static final String L_ROOT = "left_root";
    public static final String R_ROOT = "right_root";
    public static final String W_FIVE = "with_five";
    public static final String INVERSION = "inversion";

    private JsonObject pipelineObject;
    private JsonObject[] skills;
    private int currentSkillPointer;
    private int currentRootPointer;

    private String descWithBreak, descWithoutBreak;

    private Pipeline(JsonObject pipelineObject) {
        this.pipelineObject = pipelineObject;

        JsonArray skillArray = pipelineObject.getAsJsonArray(SKILLS);
        this.skills = new JsonObject[skillArray.size()];
        for(int i = 0; i < this.skills.length; i++) {
            this.skills[i] = skillArray.get(i).getAsJsonObject();
        }
        this.currentSkillPointer = 0;

        if(pipelineObject.has(HAS_ROOT) && pipelineObject.get(HAS_ROOT).getAsBoolean()) {
            this.currentRootPointer = pipelineObject.get(C_OFFSET).getAsInt();
        } else this.currentRootPointer = 0;
    }

    public static Pipeline buildPipeline(String path) {
        //create parser
        JsonParser parser = new JsonParser();
        JsonObject pipelineObject = null;
        try {
            pipelineObject = parser.parse(new FileReader(path)).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new Pipeline(pipelineObject);
    }

    public String getTitle() {
        return this.pipelineObject.get(TITLE).getAsString();
    }

    public String getDescription(boolean leaveLineBreak) {
        if(leaveLineBreak && this.descWithBreak != null) return this.descWithBreak;
        if(!leaveLineBreak && this.descWithoutBreak != null) return this.descWithoutBreak;

        StringBuilder stringBuilder = new StringBuilder();
        JsonArray descArray = this.pipelineObject.getAsJsonArray(DESC);
        for(int i = 0; i < descArray.size(); i++) {
            String currentString = descArray.get(i).getAsString();
            stringBuilder.append(currentString);
            if(leaveLineBreak && i < (descArray.size() - 1)) stringBuilder.append("\n");
            else stringBuilder.append(" ");
        }

        String out = stringBuilder.toString();
        if(leaveLineBreak) this.descWithBreak = out;
        else this.descWithoutBreak = out;

        return out;
    }

    public void nextSkill() {
        this.currentSkillPointer = (this.currentSkillPointer + 1) % this.skills.length;
    }

    public void previousSkill() {
        this.currentSkillPointer = (this.currentSkillPointer + this.skills.length - 1) % this.skills.length;
    }

    public void nextRoot() {
        JsonObject currentSkill = this.skills[this.currentSkillPointer];
        this.currentSkillPointer = this.currentSkillPointer + MusicTheory.translateCircleToChromatic(currentSkill.get(C_PROGRESS).getAsInt());
    }

    public String getCurrentSkillName() {
        return this.skills[this.currentSkillPointer].get(NAME).getAsString();
    }

    public IVoicing getCurrentVoicing(JsonObject chordObject) {
        String family = chordObject.get(FAMILY).getAsString();
        JsonObject child = chordObject.get(CHILD).getAsJsonObject();

        ChordFamily chordFamily = MusicTheory.getInstance().getChordFamily(family);
        if(child == null) return chordFamily;
        return chordFamily.getVoicing(child.getAsString());
    }

    public Note[] getLeftHand() {
        JsonObject currentSkill = this.skills[this.currentSkillPointer];
        switch(SkillType.getType(currentSkill.get(TYPE).getAsString())) {
            case CHORD -> {
                IVoicing currentVoicing = this.getCurrentVoicing(currentSkill.getAsJsonObject(CHORD));

            }
            case POLYCHORD -> {
                //...
            }
        }
        return new Note[0];
    }

    public Note[] getRightHand() {
        return new Note[0];
    }
}
