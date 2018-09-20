package cz.kalina.stampie.pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.R;
import cz.kalina.stampie.utils.Config;
import cz.kalina.stampie.utils.StampRecord;
import cz.kalina.stampie.utils.GPSLocationListener;

public class MapActivity extends FragmentActivity implements OnCameraMoveStartedListener, OnCameraMoveListener, OnCameraMoveCanceledListener, OnCameraIdleListener, OnMapReadyCallback, OnMarkerClickListener {

    private static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    private static final int LOCATION_UPDATE_MIN_TIME = 5000;

    private static GoogleMap mMap = null;
    private static LatLngBounds llb = null;

    private static List<StampRecord> stamps = null;
    private static Rect r = null;

    public static boolean doLoad = false;
    public static Map<String, String> types = new HashMap<String, String>();
    public static Map<MapPosition, List<StampRecord>> groups = null;
    public static StampRecord actStamp;
    public static StampRecord stamp;
    public static boolean stampIsGroup;
    public static List<StampRecord> stampList = null;
    public static List<StampRecord> actStampList;

    private LocationManager locationManager = null;
    private Location currentLocation = null;

    private Double myLatitude = 0.0;
    private Double myLongitude = 0.0;

    public static class MapPosition {

        public Float x;
        public Float y;

        public MapPosition() {
        }

        public MapPosition(Float _x, Float _y) {
            x = _x;
            y = _y;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;

            final MapPosition other = (MapPosition) obj;
            if ((this.x == null) ? (other.x != null) : !this.x.equals(other.x)) return false;
            if ((this.y == null) ? (other.y != null) : !this.y.equals(other.y)) return false;

            return true;
        }
    }

    public static int getTypeIcon(String typ) {

        int result = R.drawable.button_icon_map;

        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        doLoad = true;

        if (types.isEmpty()) {
            types.put("00", "Stamps");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //getCurrentLocation();

        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        try {

            double dx = Config.getMapPosX(50.080000);
            double dy = Config.getMapPosY(14.430000);
            float  dz = Config.getMapZoom((float)14);

            // animate raises onCameraChange event, then data do map will be loaded, if flag is true
            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(dx, dy)).zoom(dz).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            doLoad = true;

            MapsInitializer.initialize(this);

            moveToLocation();

        } catch (Throwable th) {
            Toast.makeText(MapActivity.this, "MapActivity onStart error: " + th.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    private void getCurrentLocation() {

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }


        if ((location == null) && isNetworkEnabled) {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (location != null) {
            currentLocation = location;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.i("Stampie", String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                currentLocation = location;
                locationManager.removeUpdates(locationListener);
            } else {
                Log.i("Stampie", "Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == OnCameraMoveStartedListener.REASON_GESTURE) {
        } else if (reason == OnCameraMoveStartedListener.REASON_API_ANIMATION) {
        } else if (reason == OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
        }
    }

    @Override
    public void onCameraMove() {
    }

    @Override
    public void onCameraMoveCanceled() {
    }

    @Override
    public void onCameraIdle() {

        try {

            Log.i("Stampie", "Map camera move detected");

            llb = mMap.getProjection().getVisibleRegion().latLngBounds;

            if (doLoad) {

                stamp = null;
                nactiDataMapy();

                MapsInitializer.initialize(this);
            }

        } catch (Exception ex) {
            Log.e("Stampie", "Error occured when processing map camera move");
        }
    }

    public boolean onMarkerClick(Marker mkr) {

        try {

            Log.i("Stampie", "Tap detected on position: " + mkr.getPosition().latitude + ", " + mkr.getPosition().longitude);

            MapPosition mkrm = new MapPosition((float)mkr.getPosition().latitude, (float)mkr.getPosition().longitude);
            stampList = null;
            for (Entry<MapPosition, List<StampRecord>> ge : groups.entrySet()) {
                if (mkrm.x.equals(ge.getKey().x) && mkrm.y.equals(ge.getKey().y)) {
                    stampList = ge.getValue();
                    break;
                }
            }
            if (stampList == null) {
                Log.e("Stampie", "OnTap: group was not found");
                return false;
            }

            doLoad = false;
            CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mkr.getPosition().latitude, mkr.getPosition().longitude))
                .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            boolean isGroup = (stampList.size() > 1);
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = null;
            MapActivity.stampIsGroup = isGroup;
            if (isGroup) {

                layout = inflater.inflate(R.layout.activity_map_group, null);

                TextView grpCntView = (TextView)layout.findViewById(R.id.LekMapsGroupCntText);
                if (grpCntView != null) grpCntView.setText(Integer.toString(stampList.size()));

            } else {

                stamp = stampList.get(0);
                layout = inflater.inflate(R.layout.activity_map_item, null);

                ImageView iconView = (ImageView)layout.findViewById(R.id.LekMapsItemIcon);
                if (iconView != null) iconView.setImageResource(MapActivity.getTypeIcon(stamp.category));

                TextView typeView = (TextView)layout.findViewById(R.id.LekMapsItemTypText);
                if (typeView != null) typeView.setText(MapActivity.types.containsKey(stamp.category) ? MapActivity.types.get(stamp.category) : "{neznámý}");

                TextView identView = (TextView)layout.findViewById(R.id.LekMapsItemJmenoText);
                if (identView != null) identView.setText(stamp.name);
            }

            layout.setOnTouchListener(new OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (MapActivity.stampIsGroup) {
                        Log.i("Stampie", "Start showing stamp list under spot");
                        Intent listIntent = new Intent(MainActivity.getContext(), MapStampListActivity.class);
                        MapActivity.actStampList = stampList;
                        MapActivity.this.startActivity(listIntent);
                    } else {
                        Intent detIntent = new Intent(MapActivity.this, MapStampDetailActivity.class);
                        MapActivity.actStamp = stamp;
                        MapActivity.this.startActivity(detIntent);
                    }

                    return true;
                }

                return true;
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
            builder.setView(layout);
            builder.show();

        } finally {
            MapActivity.doLoad = true;
            Log.i("Stampie", "Tap was processed");
        }

        return false;
    }

    private void moveToLocation() {

        try {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.2015156, 16.6798861), 10));

            Location loc = GPSLocationListener.lastLocation;
            Boolean locFound = false;
            if (loc == null) {

                try {

                    if (Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return ;
                    }

                    //retrieve a reference to an instance of TelephonyManager
                    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();

                    int cid = cellLocation.getCid();
                    int lac = cellLocation.getLac();

                    if (RqsLocation(cid, lac)) locFound = true;

                } catch (Throwable th) {
                    Log.e("Stampie", "Nelze nalezt lokaci dle GSM: " + th.getMessage());
                    locFound = false;
                }

            } else {

                myLatitude = loc.getLatitude();
                myLongitude = loc.getLongitude();

                locFound = true;
            }

            if (locFound) {
                if ((myLatitude  < 48.55) || (myLatitude  > 51.06)) locFound = false;
                if ((myLongitude < 12.08) || (myLongitude > 18.85)) locFound = false;
            }

            if (locFound) {

                //float dz = Config.getMapZoom((float)14);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(myLatitude, myLongitude))
                        .zoom(16/*dz*/)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

        } catch (Exception ex) {
            Log.e("Stampie", "Došlo k chybě při změně lokace v mapě: " + ex.getMessage());
        }
    }

    private void nactiDataMapy() {

        try {

            float dz = Config.getMapZoom((float)14);
            if (dz < 8) {
                Log.i("Stampie", "Map zoomed to far point, download is stopped");
                doLoad = true;
                return;
            }

            // startujeme working thread
            Thread download = new Thread(new Runnable() {

                public void run() {

                    try {

                        Log.i("Stampie", "Start downloading stamps data 1");
                        getMapStamps(r);

                        loadedHandler.sendMessage(new Message());

                    } catch (Throwable t) {
                        Log.e("Stampie", "Error occured in thred of download data for map: " + t.getMessage());
                    }
                }
            });
            download.start();

        } catch (Throwable th) {
            Log.e("Stampie", "Error occured when downloading data for map: " + th.getMessage());
        }
    }

    private void getMapStamps(Rect r) {

        try {

            Log.i("Stampie", "Start downloading stamps data 2");

            Float x1 = (float)llb.southwest.longitude;
            Float x2 = (float)llb.northeast.longitude;
            Float y1 = (float)llb.southwest.latitude;
            Float y2 = (float)llb.northeast.latitude;

            Log.i("Stampie", "Data downloading in rect: " + x1 + ", " + y1 + " : " + x2 + ", " + y2);
            stamps = MainActivity.GetAllStamps();
            groups = new HashMap<MapPosition, List<StampRecord>>();

            groups.clear();
            for (StampRecord m : stamps) {
                if (m == null) continue;
                List<StampRecord> xstamps = null;

                String category = "";
                for (Entry<MapPosition, List<StampRecord>> ge : groups.entrySet()) {
                    category = "";
                    List<StampRecord> groupStamps = ge.getValue();
                    if (groupStamps != null) {
                        if (groupStamps.size() > 0) {
                            StampRecord groupStamp = groupStamps.get(0);
                            if (groupStamp != null) {
                                category = groupStamp.category;
                                if (category == null) {
                                    category = "";
                                }
                            }
                        }
                    }
                    if (m.category.compareToIgnoreCase(category) == 0) {
                    //if (m.GpsPositionLat.equals(ge.getKey().x) && m.GpsPositionLng.equals(ge.getKey().y)) {
                        xstamps = ge.getValue();
                        break;
                    }
                }
                if (xstamps != null) {
                    xstamps.add(m);
                } else {
                    xstamps = new ArrayList<StampRecord>();
                    xstamps.add(m);
                    groups.put(new MapPosition(m.gpsPositionLat.floatValue(), m.gpsPositionLng.floatValue()), xstamps);
                }
            }

            Log.i("Stampie", "There was downloaded " + stamps.size() + " stamps in " + groups.size() + " groups from server");

        } catch (Exception e) {
            Log.e("Stampie", "Error occured when downloading data into map: " + e.getMessage());
        }
    }

    public static Handler loadedHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {

            try {

                Log.i("Stampie", "Stamps processing into map");
                //if (stamp == null) { Log.e("Stampie", "Cannot load list of stamps into map"); return; }

                Log.i("Stampie", "Fill map by stamp layers");
                for (Entry<MapPosition, List<StampRecord>> me : groups.entrySet()) {
                    MapPosition mk = me.getKey();
                    List<StampRecord> mv = me.getValue();
                    if (mv == null) continue;
                    if (mv.size() == 0) continue;
                    StampRecord xstamp = mv.get(0);

                    MarkerOptions mOpt = (new MarkerOptions()).position(new LatLng(mk.x.floatValue(), mk.y.floatValue()));

                    mOpt = mOpt.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_group));
/*
                    if (mv.size() > 1) mOpt = mOpt.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_group));
                    else mOpt = mOpt.icon(BitmapDescriptorFactory.fromResource(MapActivity.getTypeIcon(xstamp.category)));
*/
                    mMap.addMarker(mOpt);
                }

            } catch (Throwable th) {
                Log.e("Stampie", "Error occured when filling map by stamps: " + th.getMessage());
            } finally {
                doLoad = true;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            event.startTracking();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled()) {

            setResult(RESULT_OK);
            //finish();
            this.getParent().onBackPressed();

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private Boolean RqsLocation(int cid, int lac){

        Boolean result = false;

        String urlmmap = "http://www.google.com/glm/mmap";

        try {

            URL url = new URL(urlmmap);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.connect();

            OutputStream outputStream = httpConn.getOutputStream();
            WriteData(outputStream, cid, lac);

            InputStream inputStream = httpConn.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            dataInputStream.readShort();
            dataInputStream.readByte();

            int code = dataInputStream.readInt();
            if (code == 0) {

                Integer mLatitude = dataInputStream.readInt();
                Integer mLongitude = dataInputStream.readInt();

                myLatitude = (double)(mLatitude);
                myLongitude = (double)(mLongitude);

                result = true;
            }

        } catch (IOException e) { }

        return result;
    }

    private void WriteData(OutputStream out, int cid, int lac) throws IOException
    {
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        dataOutputStream.writeShort(21);
        dataOutputStream.writeLong(0);
        dataOutputStream.writeUTF("en");
        dataOutputStream.writeUTF("Android");
        dataOutputStream.writeUTF("1.0");
        dataOutputStream.writeUTF("Web");
        dataOutputStream.writeByte(27);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(3);
        dataOutputStream.writeUTF("");
        dataOutputStream.writeInt(cid);
        dataOutputStream.writeInt(lac);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.writeInt(0);
        dataOutputStream.flush();
    }

/*
    private static final int stampRecordColumnCount = 29;
    private GoogleMap mMap;
    private ClusterManager<StampRecord> clusterManager;
    private static List<StampRecord> stamps = null;
    private LocationManager locationManager = null;
    private Location currentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        stamps = new ArrayList<StampRecord>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation();

        clusterManager = new ClusterManager<StampRecord>(this, mMap);
        clusterManager.setRenderer(new StampRenderer());
        mMap.setOnMarkerClickListener(clusterManager);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                HttpURLConnection conn = null;
                try {
                    URL url = new URL(stampsUrl);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    // read first line with column titles
                    String line = br.readLine();

                    // read rest of document
                    while ((line = br.readLine()) != null) {

                        if (line != null) {
                            stamps.add(parseStampRecord(line));
                        }
                    }

                } catch (Exception ex) {
                    StackTraceElement[] stackTraceElements = ex.getStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {}

        // show loaded stamp marks
        for (StampRecord stamp : stamps) {
            LatLng stampPosition = new LatLng(stamp.GpsPositionLat, stamp.GpsPositionLng);
            //mMap.addMarker(new MarkerOptions().position(stampPosition).title(stamp.name).icon(BitmapDescriptorFactory.fromResource(marker_resource)));
        }

        clusterManager.addItems(stamps);
        clusterManager.cluster();
    }

    private StampRecord parseStampRecord(String stampLine) {

        StampRecord record = new StampRecord();

        String[] stampLineParts = stampLine.split(";");
        if (stampLineParts.length < stampRecordColumnCount) {

        } else {

            record.stampId = Integer.parseInt(stampLineParts[0]);
            record.name = stampLineParts[1];
            record.category = stampLineParts[2];
            record.county = stampLineParts[3];

            DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            Date date = null;
            try {
                date = format.parse(stampLineParts[4]);
            } catch (ParseException ex) {}
            if (date != null) { record.published = date; }

            record.sellingPlace1 = stampLineParts[5];
            record.sellingPlace1Web = stampLineParts[6];
            record.sellingPlace2 = stampLineParts[7];
            record.sellingPlace2Web = stampLineParts[8];
            record.sellingPlace3 = stampLineParts[9];
            record.sellingPlace3Web = stampLineParts[10];
            record.sellingPlace4 = stampLineParts[11];
            record.sellingPlace4Web = stampLineParts[12];
            record.sellingPlace5 = stampLineParts[13];
            record.sellingPlace5Web = stampLineParts[14];
            record.sellingPlace6 = stampLineParts[15];
            record.sellingPlace6Web = stampLineParts[16];
            record.sellingPlace7 = stampLineParts[17];
            record.sellingPlace7Web = stampLineParts[18];
            record.sellingPlace8 = stampLineParts[19];
            record.sellingPlace8Web = stampLineParts[20];
            record.sellingPlace9 = stampLineParts[21];
            record.sellingPlace9Web = stampLineParts[22];
            record.sellingPlace10 = stampLineParts[23];
            record.sellingPlace10Web = stampLineParts[24];
            record.sellingPlace11 = stampLineParts[25];
            record.sellingPlace11Web = stampLineParts[26];
            record.GpsPositionLat = Double.parseDouble(stampLineParts[27]);
            record.GpsPositionLng = Double.parseDouble(stampLineParts[28]);
        }

        return record;
    }

    private void getCurrentLocation() {

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }

        if (isGPSEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }


        if ((location == null) && isNetworkEnabled) {
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (location != null) {
            //Logger.d(String.format("getCurrentLocation(%f, %f)", location.getLatitude(), location.getLongitude()));
            currentLocation = location;

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));
        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //Logger.d(String.format("%f, %f", location.getLatitude(), location.getLongitude()));
                currentLocation = location;
                locationManager.removeUpdates(mLocationListener);
            } else {
                //Logger.d("Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };
*/
}
