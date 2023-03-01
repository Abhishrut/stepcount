package com.samsung.sdc21.stepcount;

import androidx.annotation.NonNull;

import com.google.android.libraries.healthdata.HealthDataClient;
import com.google.android.libraries.healthdata.data.DeleteDataRangeRequest;
import com.google.android.libraries.healthdata.data.DeleteDataRequest;
import com.google.android.libraries.healthdata.data.DeleteDataResponse;
import com.google.android.libraries.healthdata.data.InsertDataRequest;
import com.google.android.libraries.healthdata.data.InsertDataResponse;
import com.google.android.libraries.healthdata.data.ReadAggregatedDataRequest;
import com.google.android.libraries.healthdata.data.ReadAggregatedDataResponse;
import com.google.android.libraries.healthdata.data.ReadAssociatedDataRequest;
import com.google.android.libraries.healthdata.data.ReadAssociatedDataResponse;
import com.google.android.libraries.healthdata.data.ReadDataRequest;
import com.google.android.libraries.healthdata.data.ReadDataResponse;
import com.google.android.libraries.healthdata.data.ReadGroupAggregatedDataResponse;
import com.google.android.libraries.healthdata.data.ReadIntervalDataRequest;
import com.google.android.libraries.healthdata.data.ReadIntervalDataResponse;
import com.google.android.libraries.healthdata.data.ReadSampleDataRequest;
import com.google.android.libraries.healthdata.data.ReadSampleDataResponse;
import com.google.android.libraries.healthdata.data.ReadSeriesDataRequest;
import com.google.android.libraries.healthdata.data.ReadSeriesDataResponse;
import com.google.android.libraries.healthdata.data.ReadTimeGroupAggregatedDataRequest;
import com.google.android.libraries.healthdata.data.UpdateDataRequest;
import com.google.android.libraries.healthdata.data.UpdateDataResponse;
import com.google.android.libraries.healthdata.device.GetDeviceRequest;
import com.google.android.libraries.healthdata.device.GetDeviceResponse;
import com.google.android.libraries.healthdata.device.GetDevicesResponse;
import com.google.android.libraries.healthdata.device.RegisterDeviceRequest;
import com.google.android.libraries.healthdata.device.RegisterDeviceResponse;
import com.google.android.libraries.healthdata.notification.DataChangeNotificationRequest;
import com.google.android.libraries.healthdata.permission.Permission;
import com.google.common.util.concurrent.ListenableFuture;

import org.mockito.Mock;

import java.util.Set;

public final class MockHealthDataClient implements HealthDataClient {

    @Mock
    public ListenableFuture<ReadAggregatedDataResponse> readAggregatedDataResponseListenableFuture;
    public ListenableFuture<Set<Permission>> setListenableFuture;
    ListenableFuture<DeleteDataResponse> deleteDataResponseListenableFuture;
    ListenableFuture<GetDeviceResponse> getDeviceResponseListenableFuture;
    ListenableFuture<GetDevicesResponse> getDevicesResponseListenableFuture;
    ListenableFuture<InsertDataResponse> insertDataResponseListenableFuture;
    ListenableFuture<ReadAssociatedDataResponse> readAssociatedDataResponseListenableFuture;
    ListenableFuture<ReadDataResponse> readDataResponseListenableFuture;
    ListenableFuture<ReadIntervalDataResponse> readIntervalDataResponseListenableFuture;
    ListenableFuture<ReadSampleDataResponse> readSampleDataResponseListenableFuture;
    ListenableFuture<ReadSeriesDataResponse> readSeriesDataResponseListenableFuture;
    ListenableFuture<ReadGroupAggregatedDataResponse> readGroupAggregatedDataResponseListenableFuture;
    ListenableFuture<RegisterDeviceResponse> registerDeviceResponseListenableFuture;
    ListenableFuture<Void> voidListenableFuture;
    ListenableFuture<UpdateDataResponse> updateDataResponseListenableFuture;

    @NonNull
    @Override
    public ListenableFuture<DeleteDataResponse> deleteData(@NonNull DeleteDataRequest deleteDataRequest) {
        return deleteDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<DeleteDataResponse> deleteDataRange(@NonNull DeleteDataRangeRequest deleteDataRangeRequest) {
        return deleteDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<GetDeviceResponse> getDevice(@NonNull GetDeviceRequest getDeviceRequest) {
        return getDeviceResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<GetDevicesResponse> getDevices() {
        return getDevicesResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<Set<Permission>> getGrantedPermissions(@NonNull Set<Permission> set) {
        return setListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<GetDeviceResponse> getLocalDevice() {
        return getDeviceResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<InsertDataResponse> insertData(@NonNull InsertDataRequest insertDataRequest) {
        return insertDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<ReadAggregatedDataResponse> readAggregatedData(@NonNull ReadAggregatedDataRequest readAggregatedDataRequest) {
        return readAggregatedDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<ReadAssociatedDataResponse> readAssociatedData(@NonNull ReadAssociatedDataRequest readAssociatedDataRequest) {
        return readAssociatedDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<ReadDataResponse> readData(@NonNull ReadDataRequest readDataRequest) {
        return readDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<ReadIntervalDataResponse> readIntervalData(@NonNull ReadIntervalDataRequest readIntervalDataRequest) {
        return readIntervalDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<ReadSampleDataResponse> readSampleData(@NonNull ReadSampleDataRequest readSampleDataRequest) {
        return readSampleDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<ReadSeriesDataResponse> readSeriesData(@NonNull ReadSeriesDataRequest readSeriesDataRequest) {
        return readSeriesDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<ReadGroupAggregatedDataResponse> readTimeGroupAggregatedData(@NonNull ReadTimeGroupAggregatedDataRequest readTimeGroupAggregatedDataRequest) {
        return readGroupAggregatedDataResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<RegisterDeviceResponse> registerDevice(@NonNull RegisterDeviceRequest registerDeviceRequest) {
        return registerDeviceResponseListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<Void> requestDataChangeNotification(@NonNull DataChangeNotificationRequest dataChangeNotificationRequest) {
        return voidListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<Set<Permission>> requestPermissions(@NonNull Set<Permission> set) {
        return setListenableFuture;
    }

    @NonNull
    @Override
    public ListenableFuture<UpdateDataResponse> updateData(@NonNull UpdateDataRequest updateDataRequest) {
        return updateDataResponseListenableFuture;
    }
}
