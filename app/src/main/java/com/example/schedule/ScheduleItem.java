package com.example.schedule;

import java.io.Serializable;
import java.util.Date;

/**
 * Структура данных для представления элемента расписания занятий.
 * @author Abrashov
 */
public class ScheduleItem implements Serializable {
    public int id;
    public Date date;
    public int pairNumber;
    public int groupId;
    public String group;
    public int teacherId;
    public String teacher;
    public int disciplineId;
    public String discipline;
    public int auditoryId;
    public String auditory;

    @Override
    public String toString() {
        return pairNumber + ")" + " гр. " + group.toString() + ", " + discipline.toString() + ", "
                + teacher.toString() + ", к." + auditory.toString();
    }
}
