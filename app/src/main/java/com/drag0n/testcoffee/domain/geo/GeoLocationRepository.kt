package com.drag0n.testcoffee.domain.geo

interface GeoLocationRepository {
    fun chekPermissionLocation() : Boolean
    fun isLocationEnabled(): Boolean
}