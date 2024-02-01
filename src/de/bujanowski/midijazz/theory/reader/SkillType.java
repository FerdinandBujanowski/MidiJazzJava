package de.bujanowski.midijazz.theory.reader;

public enum SkillType {

    CHORD(Pipeline.CHORD), POLYCHORD("polychord");

    private String typeName;

    SkillType(String typeName) {
        this.typeName = typeName;
    }

    public static SkillType getType(String typeName) {
        for(SkillType skillType : SkillType.values()) {
            if(skillType.typeName.equals(typeName)) return skillType;
        }
        return SkillType.CHORD;
    }
}
