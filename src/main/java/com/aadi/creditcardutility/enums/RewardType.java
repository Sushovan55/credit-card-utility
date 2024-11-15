package com.aadi.creditcardutility.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RewardType {
    AirtelAxisInternet(0.25),
    AirtelAxisUtility(0.1),
    AirtelAxisFood(0.1),
    AirtelAxisBase(0.01);

    public final Double reward;

    @Override
    public String toString() {
        return super.toString() + "(" + getReward() + ")";
    }
}
