package com.samsung.sdc21.stepcount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.android.libraries.healthdata.data.IntervalDataTypes;
import com.google.android.libraries.healthdata.permission.AccessType;
import com.google.android.libraries.healthdata.permission.Permission;
import com.google.common.util.concurrent.ListenableFuture;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class PermissionsUnitTest {

    @Test
    public void getGrantedPermissionsWillThrowExceptionWhenHealthDataClientIsNullTest_N() {
        Permissions permissions = new Permissions(null);
        Throwable exception = assertThrows(PermissionsException.class, permissions::getGrantedPermissions);
        assertEquals("health client is null", exception.getMessage());
    }

    @Test
    public void getGrantedPermissionsWillReturnCorrectObject_P() {
        MockHealthDataClient mockHealthDataClient = new MockHealthDataClient();
        Permissions permissions = new Permissions(mockHealthDataClient);
        try {
            ListenableFuture<Set<Permission>> result = permissions.getGrantedPermissions();
            assertEquals(mockHealthDataClient.setListenableFuture, result);
        } catch (PermissionsException exception) {
            assert (false);
        }
    }


    @Test
    public void requestPermissionsWillThrowExceptionWhenHealthDataClientIsNullTest_N() {
        Permissions permissions = new Permissions(null);
        Throwable exception = assertThrows(PermissionsException.class, permissions::requestPermissions);
        assertEquals("health client is null", exception.getMessage());
    }

    @Test
    public void requestPermissionsWillReturnCorrectObject_P() {
        MockHealthDataClient mockHealthDataClient = new MockHealthDataClient();
        Permissions permissions = new Permissions(mockHealthDataClient);
        try {
            ListenableFuture<Set<Permission>> result = permissions.requestPermissions();
            assertEquals(mockHealthDataClient.setListenableFuture, result);
        } catch (PermissionsException exception) {
            assert (false);
        }
    }

    @Test
    public void arePermissionsGrantedWillReturnFalseWhenParameterIsNull_N() {
        Permissions permissions = new Permissions(null);
        boolean result = permissions.arePermissionsGranted(null);
        assertFalse(result);
    }

    @Test
    public void arePermissionsGrantedWillReturnFalseWhenPermissionsSetIsEmpty_N() {
        Permissions permissions = new Permissions(null);
        Set<Permission> permissionsResult = new HashSet<>();
        boolean result = permissions.arePermissionsGranted(permissionsResult);
        assertFalse(result);
    }

    @Test
    public void arePermissionsGrantedWillReturnFalseWhenPermissionsSetDoNotContainsStepsReadPermission_P() {
        Permissions permissions = new Permissions(null);
        Set<Permission> permissionsResult = new HashSet<>();
        Permission activityTimeReadPermission = Permission.builder()
                .setDataType(IntervalDataTypes.ACTIVE_TIME)
                .setAccessType(AccessType.READ)
                .build();
        permissionsResult.add(activityTimeReadPermission);
        boolean result = permissions.arePermissionsGranted(permissionsResult);
        assertFalse(result);
    }

    @Test
    public void arePermissionsGrantedWillReturnTrueWhenPermissionsSetContainsStepsReadPermission_P() {
        Permissions permissions = new Permissions(null);
        Set<Permission> permissionsResult = new HashSet<>();
        Permission activityTimeReadPermission = Permission.builder()
                .setDataType(IntervalDataTypes.ACTIVE_TIME)
                .setAccessType(AccessType.READ)
                .build();
        Permission stepsReadPermission = Permission.builder()
                .setDataType(IntervalDataTypes.STEPS)
                .setAccessType(AccessType.READ)
                .build();
        permissionsResult.add(activityTimeReadPermission);
        permissionsResult.add(stepsReadPermission);
        boolean result = permissions.arePermissionsGranted(permissionsResult);
        assertTrue(result);
    }
}
