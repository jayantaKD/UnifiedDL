package org.infobeyondtech.unifieddl.metadatamodel.ingestion;

import org.infobeyondtech.unifieddl.metadatamodel.processandaccess.ETLFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EntityClass {
    int id;
    public String name;
    public boolean isETLTransformationApplied;
    public ETLFunction trasformationFunction;
    public String[] appliedABACPolicies;
    String comment;
    int numberOfAttributes;
    int numberOfNumericAttributes;
    int numberOfNominalAttributes;
    int numberOfInstances;
    // number of instances (i.e., rows) having one or more missing values
    int numberOfInstancesWithMissingValues;
    // total number of missing values
    int numberOfMissingValues;
    // 1..* composite aggregation relation
    List<Attribute> associatedAttributes = new LinkedList<Attribute>();
    // bi-directional association 0..1 to 0..1
    SensitivityMark associatedSensitivityMark;

    private List<NumericAttribute> getAssociatedNumericAttributes(){
        List<NumericAttribute> res = new ArrayList<NumericAttribute>();
        for(int i=0; i<associatedAttributes.size(); i++){
            Attribute pick = associatedAttributes.get(i);
             if(pick.attributeType == AttributeType.NumericAttribute){
                 res.add((NumericAttribute) pick);
             }
        }
        return res;
    }

    private List<NominalAttribute> getAssociatedNominalAttributes(){
        List<NominalAttribute> res = new ArrayList<NominalAttribute>();
        for(int i=0; i<associatedAttributes.size(); i++){
            Attribute pick = associatedAttributes.get(i);
            if(pick.attributeType == AttributeType.NominalAttribute){
                res.add((NominalAttribute) pick);
            }
        }
        return res;
    }

    // number of attributes of the entity class
    public int dimensionality(){
        return this.numberOfAttributes;
    }

    public float percentageOfInstancesWithMissingValues(){
        return (this.numberOfInstancesWithMissingValues/this.numberOfAttributes)*100;
    }

    public float percentageOfMissingValues(){
        return (this.numberOfMissingValues/(this.numberOfAttributes*this.numberOfInstances))*100;
    }

    public float meanMeansOfNumericAtts(){
        float res = 0;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i<associatedNumericAttributes.size(); i++){
            res = res + associatedNumericAttributes.get(i).mean;
        }
        return res/associatedNumericAttributes.size();
    }

    public float meanStdDevOfNumericAtts(){
        float res = 0;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i<associatedNumericAttributes.size(); i++){
            res = res + associatedNumericAttributes.get(i).standardDeviation;
        }
        return res/associatedNumericAttributes.size();
    }

    public float meanKurtosisOfNumericAtts(){
        float res = 0;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i<associatedNumericAttributes.size(); i++){
            res = res + associatedNumericAttributes.get(i).kurtosis;
        }
        return res/associatedNumericAttributes.size();
    }

    public float meanSkewnessOfNumericAtts(){
        float res = 0;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i<associatedNumericAttributes.size(); i++){
            res = res + associatedNumericAttributes.get(i).skewness;
        }
        return res/associatedNumericAttributes.size();
    }

    public float minMeansOfNumericAtts(){
        float res = Float.MAX_VALUE;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i<associatedNumericAttributes.size(); i++){
            res = Float.min(res, associatedNumericAttributes.get(i).mean) ;
        }
        return res;
    }

    public float minStdDevOfNumericAtts(){
        float res = Float.MAX_VALUE;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i<associatedNumericAttributes.size(); i++){
            res = Float.min(res, associatedNumericAttributes.get(i).standardDeviation) ;
        }
        return res;
    }

    public float minKurtosisOfNumericAtts(){
        float res = Float.MAX_VALUE;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i < associatedNumericAttributes.size(); i++){
            res = Float.min(res, associatedNumericAttributes.get(i).kurtosis) ;
        }
        return res;
    }

    public float minSkewnessOfNumericAtts(){
        float res = Float.MAX_VALUE;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i < associatedNumericAttributes.size(); i++){
            res = Float.min(res, associatedNumericAttributes.get(i).skewness) ;
        }
        return res;
    }

    public float maxMeansOfNumericAtts(){
        float res = Float.MIN_VALUE;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i < associatedNumericAttributes.size(); i++){
            res = Float.max(res, associatedNumericAttributes.get(i).mean) ;
        }
        return res;
    }

    public float maxStdDevOfNumericAtts(){
        float res = Float.MIN_VALUE;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i < associatedNumericAttributes.size(); i++){
            res = Float.max(res, associatedNumericAttributes.get(i).standardDeviation) ;
        }
        return res;
    }

    public float maxKurtosisOfNumericAtts(){
        float res = Float.MIN_VALUE;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i < associatedNumericAttributes.size(); i++){
            res = Float.max(res, associatedNumericAttributes.get(i).kurtosis) ;
        }
        return res;
    }

    public float maxSkewnessOfNumericAtts(){
        float res = Float.MIN_VALUE;
        List<NumericAttribute> associatedNumericAttributes = this.getAssociatedNumericAttributes();
        for(int i=0; i < associatedNumericAttributes.size(); i++){
            res = Float.max(res, associatedNumericAttributes.get(i).skewness) ;
        }
        return res;
    }

    public float quartile1MeansOfNumericAtts(){
        return 0;
    }
    public float quartile1StdDevOfNumericAtts(){
        return 0;
    }
    public float quartile1KurtosisOfNumericAtts(){
        return 0;
    }
    public float quartile1SkewnessOfNumericAtts(){
        return 0;
    }
    public float quartile2MeansOfNumericAtts(){
        return 0;
    }
    public float quartile2StdDevOfNumericAtts(){
        return 0;
    }
    public float quartile2KurtosisOfNumericAtts(){
        return 0;
    }
    public float quartile2SkewnessOfNumericAtts(){
        return 0;
    }
    public float quartile3MeansOfNumericAtts(){
        return 0;
    }
    public float quartile3StdDevOfNumericAtts(){
        return 0;
    }
    public float quartile3KurtosisOfNumericAtts(){
        return 0;
    }
    public float quartile3SkewnessOfNumericAtts(){
        return 0;
    }
    public float meanAttributeEntropy(){
        return 0;
    }
    public float minAttributeEntropy(){
        return 0;
    }
    public float maxAttributeEntropy(){
        return 0;
    }
    public float quartile1AttributeEntropy(){
        return 0;
    }
    public float quartile2AttributeEntropy(){
        return 0;
    }
    public float quartile3AttributeEntropy(){
        return 0;
    }
    public float maxNominalAttDistinctValues(){
        return 0;
    }
    public float minNominalAttDistinctValues(){
        return 0;
    }
    public float meanNominalAttDistinctValues(){
        return 0;
    }
    public float stdvNominalAttDistinctValues(){
        return 0;
    }
}
