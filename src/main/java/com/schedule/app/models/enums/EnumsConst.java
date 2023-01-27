package com.schedule.app.models.enums;

/**
 * @author : Thành Lộc
 * @since : 1/26/2022, Fri
 **/
public class EnumsConst {
    public EnumsConst() {
    }

    public static enum ExamType {

        ESSAY("Tự luận"),
        COMPUTATIONAL("Thực hành"),
        ORAL("Vấn đáp");

        String description;

        private ExamType(final String description) {
            this.description = description;
        }
        public String getDescription() {
            return this.description;
        }
    }

}
