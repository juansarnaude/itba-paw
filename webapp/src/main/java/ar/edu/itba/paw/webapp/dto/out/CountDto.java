package ar.edu.itba.paw.webapp.dto.out;

public class CountDto {

    private int count;

    public static CountDto fromCount(int count) {
        CountDto dto = new CountDto();
        dto.count = count;
        return dto;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
