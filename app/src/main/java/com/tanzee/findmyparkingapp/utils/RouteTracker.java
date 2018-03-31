package com.tanzee.findmyparkingapp.utils;

import com.tanzee.findmyparkingapp.dto.MapDirectionResponseDto;

/**
 * Created by Tushar on 7/30/2017.
 */

public class RouteTracker{

    private MapDirectionResponseDto.routes routes;
    private int tracker;

    public RouteTracker(MapDirectionResponseDto.routes routes, int tracker){
        this.routes = routes;
        this.tracker = tracker;
    }

    public MapDirectionResponseDto.routes getRoutes(){
        return routes;
    }

    public void setRoutes(MapDirectionResponseDto.routes routes){
        this.routes = routes;
    }

    public int getTracker(){
        return tracker;
    }

    public void setTracker(int tracker){
        this.tracker = tracker;
    }
}
