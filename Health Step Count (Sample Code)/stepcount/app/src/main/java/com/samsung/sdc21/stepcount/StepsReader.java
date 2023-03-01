package com.samsung.sdc21.stepcount;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.libraries.healthdata.HealthDataClient;
import com.google.android.libraries.healthdata.data.AggregatedValue;
import com.google.android.libraries.healthdata.data.CumulativeAggregationSpec;
import com.google.android.libraries.healthdata.data.CumulativeData;
import com.google.android.libraries.healthdata.data.IntervalDataTypes;
import com.google.android.libraries.healthdata.data.ReadAggregatedDataRequest;
import com.google.android.libraries.healthdata.data.ReadAggregatedDataResponse;
import com.google.android.libraries.healthdata.data.TimeSpec;
import com.google.common.util.concurrent.ListenableFuture;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class StepsReader {
    private final HealthDataClient healthDataClient;

    StepsReader(HealthDataClient healthDC) {
        healthDataClient = healthDC;
    }

    public ListenableFuture<ReadAggregatedDataResponse> readAggregatedData() throws StepsReaderException {
        if (healthDataClient == null) {
            throw new StepsReaderException("health client is null");
        }
        /*******************************************************************************************
         * [Practice 2] Build read aggregated data request object for read today's steps
         *  - Set interval data type of steps
         -------------------------------------------------------------------------------------------
         *  - (Hint) Uncomment line 42 and fill below TODO 2 with
         *      (1) for interval data type: IntervalDataTypes.STEPS
         ******************************************************************************************/
        ReadAggregatedDataRequest readAggregatedDataRequest = ReadAggregatedDataRequest.builder()
                .setTimeSpec(
                        TimeSpec.builder()
                                .setStartLocalDateTime(LocalDateTime.now().with(LocalTime.MIDNIGHT))
                                .build())
                .addCumulativeAggregationSpec(CumulativeAggregationSpec.builder(IntervalDataTypes.STEPS).build())
                .build();
        return healthDataClient.readAggregatedData(readAggregatedDataRequest);
    }

    public long readStepsFromAggregatedDataResponse(@Nullable ReadAggregatedDataResponse result) {
        /*******************************************************************************************
         * [Practice 3] Read aggregated value from cumulative data and add them to the result
         *  - Get AggregatedValue from cumulativeData object
         *  - Get steps count from AggregatedValue object
         -------------------------------------------------------------------------------------------
         *  - (Hint) Replace TODO 3 with parts of code
         *      (1) get AggregatedValue object 'obj' using cumulativeData.getTotal()
         *      (2) get value using obj.getLongValue() and add to the result
         ******************************************************************************************/
        long steps = 0L;

        if (result != null) {
            List<CumulativeData> cumulativeDataList = result.getCumulativeDataList();
            if (!cumulativeDataList.isEmpty()) {
                for (CumulativeData cumulativeData : cumulativeDataList) {
                    AggregatedValue obj = cumulativeData.getTotal();
                    if(obj!=null){
                        Log.e("ABHI", "obj not null");
                        steps+=obj.getLongValue();
                    }
                    else {
                        Log.e("ABHI", "obj null");
                    }

                }
            }
        }
        return steps;
    }
}
