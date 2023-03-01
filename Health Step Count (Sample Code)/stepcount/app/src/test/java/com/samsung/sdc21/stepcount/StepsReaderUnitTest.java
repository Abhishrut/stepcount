package com.samsung.sdc21.stepcount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static java.time.temporal.ChronoUnit.HOURS;

import com.google.android.libraries.healthdata.data.AggregatedValue;
import com.google.android.libraries.healthdata.data.CumulativeData;
import com.google.android.libraries.healthdata.data.IntervalDataTypes;
import com.google.android.libraries.healthdata.data.ReadAggregatedDataResponse;
import com.google.common.util.concurrent.ListenableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;

@RunWith(MockitoJUnitRunner.class)
public class StepsReaderUnitTest {

    @Test
    public void readAggregatedDataWillThrowExceptionWhenHealthDataClientIsNullTest_N() {
        StepsReader stepsReader = new StepsReader(null);
        Throwable exception = assertThrows(StepsReaderException.class, stepsReader::readAggregatedData);
        assertEquals("health client is null", exception.getMessage());
    }

    @Test
    public void readAggregatedDataWillReturnCorrectObject_P() {
        MockHealthDataClient mockHealthDataClient = new MockHealthDataClient();
        StepsReader stepsReader = new StepsReader(mockHealthDataClient);
        try {
            ListenableFuture<ReadAggregatedDataResponse> result = stepsReader.readAggregatedData();
            assertEquals(mockHealthDataClient.readAggregatedDataResponseListenableFuture, result);
        } catch (StepsReaderException exception) {
            assert (false);
        }
    }

    @Test
    public void readStepsFromAggregatedDataResponseWillReturnZeroWhenParameterIsNull_N() {
        StepsReader stepsReader = new StepsReader(null);
        final long stepsCount = stepsReader.readStepsFromAggregatedDataResponse(null);
        assertEquals(0L, stepsCount);
    }

    @Test
    public void readStepsFromAggregatedDataResponseWillReturnZeroWhenListIsEmpty_N() {
        StepsReader stepsReader = new StepsReader(null);
        ReadAggregatedDataResponse readAggregatedDataResponse = ReadAggregatedDataResponse.builder()
                .build();
        final long stepsCount = stepsReader.readStepsFromAggregatedDataResponse(readAggregatedDataResponse);
        assertEquals(0L, stepsCount);
    }

    @Test
    public void readStepsFromAggregatedDataResponseWillReturnStepsCount_P() {
        StepsReader stepsReader = new StepsReader(null);
        AggregatedValue aggregatedValue = AggregatedValue.builder(IntervalDataTypes.STEPS)
                .setLongValue(100L)
                .build();
        CumulativeData cumulativeData = CumulativeData.builder(IntervalDataTypes.STEPS)
                .setTotal(aggregatedValue)
                .setStartTime(Instant.now().minus(3, HOURS))
                .setEndTime(Instant.now().minus(2, HOURS))
                .build();
        ReadAggregatedDataResponse readAggregatedDataResponse = ReadAggregatedDataResponse.builder()
                .addCumulativeData(cumulativeData)
                .build();
        final long stepsCount = stepsReader.readStepsFromAggregatedDataResponse(readAggregatedDataResponse);
        assertEquals(100L, stepsCount);
    }

    @Test
    public void readStepsFromAggregatedDataResponseWillReturnStepsCountFromAllCumulativeDataListElements_P() {
        StepsReader stepsReader = new StepsReader(null);
        AggregatedValue aggregatedValue1 = AggregatedValue.builder(IntervalDataTypes.STEPS)
                .setLongValue(100L)
                .build();
        AggregatedValue aggregatedValue2 = AggregatedValue.builder(IntervalDataTypes.STEPS)
                .setLongValue(50L)
                .build();
        CumulativeData cumulativeData1 = CumulativeData.builder(IntervalDataTypes.STEPS)
                .setTotal(aggregatedValue1)
                .setStartTime(Instant.now().minus(3, HOURS))
                .setEndTime(Instant.now().minus(2, HOURS))
                .build();
        CumulativeData cumulativeData2 = CumulativeData.builder(IntervalDataTypes.STEPS)
                .setTotal(aggregatedValue2)
                .setStartTime(Instant.now().minus(5, HOURS))
                .setEndTime(Instant.now().minus(4, HOURS))
                .build();
        ReadAggregatedDataResponse readAggregatedDataResponse = ReadAggregatedDataResponse.builder()
                .addCumulativeData(cumulativeData1)
                .addCumulativeData(cumulativeData2)
                .build();
        final long stepsCount = stepsReader.readStepsFromAggregatedDataResponse(readAggregatedDataResponse);
        assertEquals(150L, stepsCount);
    }
}
