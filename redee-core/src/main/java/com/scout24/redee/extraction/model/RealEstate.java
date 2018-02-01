package com.scout24.redee.extraction.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by dprawdzik on 10.01.18.
 */
public class RealEstate {
    @SerializedName("@id")
    public String id;
    public Date creationDate;
    public Date lastModificationDate;
    public String descriptionNote;
    public String furnishingNote;
    public String locationNote;
    public String otherNote;

    public String apartmentType;
    private boolean lift;
    private boolean assistedLiving;
    private String cellar;
    private String handicappedAccessible;
    private String condition;
    private String interiorQuality;
    private int constructionYear;

    private float numberOfBathRooms;
    private float numberOfBedRooms;

    private float totalRent;
    private float baseRent;
    private float livingSpace;

    private boolean garden;
    private boolean balcony;
    private boolean builtInKitchen;
    private float numberOfRooms;
    private String useAsFlatshareRoom;
    private String guestToilet;
    private String parkingSpaceType;

    public String getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public String getDescriptionNote() {
        return descriptionNote;
    }

    public String getFurnishingNote() {
        return furnishingNote;
    }

    public String getLocationNote() {
        return locationNote;
    }

    public String getOtherNote() {
        return otherNote;
    }

    public String getApartmentType() {
        return apartmentType;
    }

    public boolean isLift() {
        return lift;
    }

    public boolean isAssistedLiving() {
        return assistedLiving;
    }

    public String getCellar() {
        return cellar;
    }

    public String getHandicappedAccessible() {
        return handicappedAccessible;
    }

    public String getCondition() {
        return condition;
    }

    public String getInteriorQuality() {
        return interiorQuality;
    }

    public int getConstructionYear() {
        return constructionYear;
    }

    public float getNumberOfBathRooms() {
        return numberOfBathRooms;
    }

    public float getNumberOfBedRooms() {
        return numberOfBedRooms;
    }

    public float getTotalRent() {
        return totalRent;
    }

    public float getBaseRent() {
        return baseRent;
    }

    public float getLivingSpace() {
        return livingSpace;
    }

    public boolean hasGarden() {
        return garden;
    }

    public boolean hasBalcony() {
        return balcony;
    }

    public boolean hasBuiltInKitchen() {
        return builtInKitchen;
    }

    public float getNumberOfRooms() {
        return numberOfRooms;
    }

    public String getUseAsFlatshareRoom() {
        return useAsFlatshareRoom;
    }

    public String getGuestToilet() {
        return guestToilet;
    }

    public String getParkingSpaceType() {
        return parkingSpaceType;
    }
/*"floor": 0,
        "energyCertificate": {
        "energyCertificateAvailability": "AVAILABLE",
                "energyCertificateCreationDate": "BEFORE_01_MAY_2014"
    },

            "heatingTypeEnev2014": "GAS_HEATING",
            "energySourcesEnev2014": {
        "energySourceEnev2014": "ACID_GAS"
    },
            "buildingEnergyRatingType": "ENERGY_REQUIRED",
            "thermalCharacteristic": 125,
            "energyConsumptionContainsWarmWater": "NOT_APPLICABLE",
            "numberOfFloors": 3,
            "usableFloorSpace": 85.42,
            "numberOfBedRooms": 1,
            "numberOfBathRooms": 1,
            "guestToilet": "NOT_APPLICABLE",
            "parkingSpaceType": "OUTSIDE",
            "baseRent": 755.11,
            "totalRent": 970.11,
            "serviceCharge": 125,
            "deposit": "2.265,33 Euro",
            "heatingCosts": 90,
            "heatingCostsInServiceCharge": "NO",
            "calculatedTotalRent": 970.11,
            "calculatedTotalRentScope": "WARM_RENT",
            "petsAllowed": "NEGOTIABLE",
            "useAsFlatshareRoom": "NOT_APPLICABLE",
            "livingSpace": 85.42,
            "numberOfRooms": 3,
            "energyPerformanceCertificate": "true",
            "builtInKitchen": "true",
            "balcony": "true",
            "certificateOfEligibilityNeeded": "false",
            "garden": "false",
            "courtage": {
        "hasCourtage": "NOT_APPLICABLE"
    }
},*/


}
