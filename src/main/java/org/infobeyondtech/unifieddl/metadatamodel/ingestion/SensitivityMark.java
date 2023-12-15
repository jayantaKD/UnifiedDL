package org.infobeyondtech.unifieddl.metadatamodel.ingestion;


public class SensitivityMark {
    public String description;
    // bi-directional association 0..n to 1
    // https://learn.microsoft.com/en-us/purview/sensitivity-labels
    // Sensitivity
    public SensitivityLevel hasSensitivityLevel;
    // bi-directional association 0..1 to 0..1
    public Attribute associatedAttribute;
}