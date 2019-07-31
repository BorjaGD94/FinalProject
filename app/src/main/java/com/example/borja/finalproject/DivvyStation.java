package com.example.borja.finalproject;


public class DivvyStation
{

        private String station_name;

        private String address;

        private String docks_in_service;

        private String latitude;

        private String total_docks;

        private LocationDivvy location;

        private String id;

        private String status;

        private String longitude;

        public String getStation_name ()
        {
            return station_name;
        }

        public void setStation_name (String station_name)
        {
            this.station_name = station_name;
        }

        public String getAddress ()
        {
            return address;
        }

        public void setAddress (String address)
        {
            this.address = address;
        }

        public String getDocks_in_service ()
        {
            return docks_in_service;
        }

        public void setDocks_in_service (String docks_in_service)
        {
            this.docks_in_service = docks_in_service;
        }

        public String getLatitude ()
        {
            return latitude;
        }

        public void setLatitude (String latitude)
        {
            this.latitude = latitude;
        }

        public String getTotal_docks ()
        {
            return total_docks;
        }

        public void setTotal_docks (String total_docks)
        {
            this.total_docks = total_docks;
        }

        public LocationDivvy getLocation ()
        {
            return location;
        }

        public void setLocation (LocationDivvy location)
        {
            this.location = location;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public String getLongitude ()
        {
            return longitude;
        }

        public void setLongitude (String longitude)
        {
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "DivvyStation{" +
                    "station_name='" + station_name + '\'' +
                    ", address='" + address + '\'' +
                    ", docks_in_service='" + docks_in_service + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", total_docks='" + total_docks + '\'' +
                    ", location=" + location +
                    ", id='" + id + '\'' +
                    ", status='" + status + '\'' +
                    ", longitude='" + longitude + '\'' +
                    '}';
        }
    }



