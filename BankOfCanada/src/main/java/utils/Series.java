package utils;

import java.time.LocalDate;

public class Series {

    private LocalDate date;
    private String seriesName;
    private String label;
    private String description;
    private double value;

    /**
     * constructor to create a new series object
     * @param date - The date
     * @param seriesName - The series name
     * @param label - The series label
     * @param description - The series description
     * @param value - The value
     */
    public Series(LocalDate date, String seriesName, String label, String description, double value) {
        this.date = date;
        this.seriesName = seriesName;
        this.label = label;
        this.description = description;
        this.value = value;
    }

    /**
     *
     * @return the series name
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * set the name of the series
     * @param seriesName - the name of the series
     */
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    /**
     *
     * @return the series label
     */
    public String getLabel() {
        return label;
    }

    /**
     * set the label for the series
     * @param label - the label of the series
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @return the series description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description of the series
     * @param description - the description of the series
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * set the date
     * @param date - the date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     *
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * set the value
     * @param value - the exchange rate
     */
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return date.toString() + ',' +
                seriesName + ',' +
                label + ',' +
                description + "," +
                value;
    }
}
