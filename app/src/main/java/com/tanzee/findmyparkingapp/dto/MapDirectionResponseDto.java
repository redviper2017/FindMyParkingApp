package com.tanzee.findmyparkingapp.dto;

import java.util.List;


public class MapDirectionResponseDto{

    String status;

    List<geocoded_waypoints> geocoded_waypoints;
    List<routes> routes;

    public List<MapDirectionResponseDto.routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<MapDirectionResponseDto.routes> routes) {
        this.routes = routes;
    }

    public List<MapDirectionResponseDto.geocoded_waypoints> getGeocoded_waypoints() {
        return geocoded_waypoints;
    }

    public void setGeocoded_waypoints(List<MapDirectionResponseDto.geocoded_waypoints> geocoded_waypoints) {
        this.geocoded_waypoints = geocoded_waypoints;
    }

    public class geocoded_waypoints{
        String geocoder_status, place_id;

        List<String> types;

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getGeocoder_status() {
            return geocoder_status;
        }

        public void setGeocoder_status(String geocoder_status) {
            this.geocoder_status = geocoder_status;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        @Override
        public String toString() {
            return "geocoded_waypoints{" +
                    "geocoder_status='" + geocoder_status + '\'' +
                    ", place_id='" + place_id + '\'' +
                    '}';
        }
    }

    public class routes{

        String copyrights;

        bounds bounds;

        List<legs> legs;

        public List<MapDirectionResponseDto.routes.legs> getLegs() {
            return legs;
        }

        public void setLegs(List<MapDirectionResponseDto.routes.legs> legs) {
            this.legs = legs;
        }

        public class bounds{

            northeast northeast;
            southwest southwest;

            public bounds.northeast getNortheast() {
                return northeast;
            }

            public void setNortheast(bounds.northeast northeast) {
                this.northeast = northeast;
            }

            public bounds.southwest getSouthwest() {
                return southwest;
            }

            public void setSouthwest(bounds.southwest southwest) {
                this.southwest = southwest;
            }

            public class northeast{

                double lat, lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

            }
            public class southwest{

                double lat, lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

            }
        }

        public class legs{

            String end_address, start_address;

            List<steps> steps;

            distance distance;

            public String getEnd_address(){
                return end_address;
            }

            public void setEnd_address(String end_address){
                this.end_address = end_address;
            }

            public List<MapDirectionResponseDto.routes.legs.steps> getSteps() {
                return steps;
            }

            public void setSteps(List<MapDirectionResponseDto.routes.legs.steps> steps) {
                this.steps = steps;
            }

            public distance getD(){
                return distance;
            }

            public void setD(distance distance){
                this.distance = distance;
            }

            public class distance{
                String text;
                int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public class duration{
                String text;
                double value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }

            public class end_location{

                double lat, lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

            }

            public class start_location{

                double lat, lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

            }

            public class steps{

                String html_instructions, travel_mode;

                end_location end_location;
                start_location start_location;

                public MapDirectionResponseDto.routes.legs.steps.start_location getStart_location() {
                    return start_location;
                }

                public void setStart_location(MapDirectionResponseDto.routes.legs.steps.start_location start_location) {
                    this.start_location = start_location;
                }

                public String getHtml_instructions() {
                    return html_instructions;
                }

                public void setHtml_instructions(String html_instructions) {
                    this.html_instructions = html_instructions;
                }

                public String getTravel_mode() {
                    return travel_mode;
                }

                public void setTravel_mode(String travel_mode) {
                    this.travel_mode = travel_mode;
                }

                public MapDirectionResponseDto.routes.legs.steps.end_location getEnd_location() {
                    return end_location;
                }

                public void setEnd_location(MapDirectionResponseDto.routes.legs.steps.end_location end_location) {
                    this.end_location = end_location;
                }

                public class distance{
                    String text;
                    int value;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public int getValue() {
                        return value;
                    }

                    public void setValue(int value) {
                        this.value = value;
                    }
                }

                public class duration{
                    String text;
                    int value;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public int getValue() {
                        return value;
                    }

                    public void setValue(int value) {
                        this.value = value;
                    }
                }

                public class end_location{
                    double lat, lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public class polyline{
                    String points;

                    public String getPoints() {
                        return points;
                    }

                    public void setPoints(String points) {
                        this.points = points;
                    }
                }
                public class start_location{
                    double lat, lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

            }
        }

        public class overview_polyline{
            String points;

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }
        }

    }


}
