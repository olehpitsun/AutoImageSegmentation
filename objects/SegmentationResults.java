package objects;

/**
 * Created by oleh on 19.12.2016.
 */
public class SegmentationResults {

    public  String imgName;
    public  Integer lowThresh;
    public  Integer lastID;
    public  Float histogramAverage;
    public  Double redAverage;
    public  Double greenAverage;
    public  Double blueAverage;
    public  Double distance;
    public  Double FRAG;
    public  Integer human_evaluate;

    public SegmentationResults(Integer lastID, String imgName, Integer lowThresh, Double distance, Double FRAG, Integer human_evaluate) {
        this.lastID = lastID;
        this.imgName = imgName;
        this.lowThresh = lowThresh;
        this.distance = distance;
        this.FRAG = FRAG;
        this.human_evaluate = human_evaluate;
    }

    public Integer getLastID() {
        return lastID;
    }

    public void setLastID(Integer lastID) {
        this.lastID = lastID;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Integer getLowThresh() {
        return lowThresh;
    }

    public void setLowThresh(Integer lowThresh) {
        this.lowThresh = lowThresh;
    }

    public Float getHistogramAverage() {
        return histogramAverage;
    }

    public void setHistogramAverage(Float histogramAverage) {
        this.histogramAverage = histogramAverage;
    }

    public Double getRedAverage() {
        return redAverage;
    }

    public void setRedAverage(Double redAverage) {
        this.redAverage = redAverage;
    }

    public Double getGreenAverage() {
        return greenAverage;
    }

    public void setGreenAverage(Double greenAverage) {
        this.greenAverage = greenAverage;
    }

    public Double getBlueAverage() {
        return blueAverage;
    }

    public void setBlueAverage(Double blueAverage) {
        this.blueAverage = blueAverage;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getFRAG() {
        return FRAG;
    }

    public void setFRAG(Double FRAG) {
        this.FRAG = FRAG;
    }

    public Integer getHuman_evaluate() {
        return human_evaluate;
    }

    public void setHuman_evaluate(Integer human_evaluate) {
        this.human_evaluate = human_evaluate;
    }

    @Override
    public String toString() {
        return "SegmentationResults{" +
                "imgName='" + imgName + '\'' +
                ", lowThresh=" + lowThresh +
                ", lastID=" + lastID +
                ", distance=" + distance +
                ", FRAG=" + FRAG +
                ", human_evaluate=" + human_evaluate +
                '}';
    }
}
