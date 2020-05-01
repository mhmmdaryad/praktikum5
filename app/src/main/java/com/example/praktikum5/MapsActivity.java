package com.example.praktikum5;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.praktikum5.nearbyplaces.GetNearbyPlacesData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocompleteFragment;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng malang = new LatLng(-7.930060, 112.638376);
    private static final int PROXIMITY_RADIUS_METERS = 15000;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
        setupAutoCompleteFragment();
    }

    private void setupAutoCompleteFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                malang = place.getLatLng();

                String url = getUrl(malang.latitude, malang.longitude, "");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);
            }

            @Override
            public void onError(Status status) {
                Log.e("Error", status.getStatusMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(malang, 8.5f));
        mMap.addMarker(new MarkerOptions()
                .position(malang)
                .title("wang")
                .snippet("lokasi saya")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        LatLng gasStation1 = new LatLng(-7.940475,112.649334);
        mMap.addMarker(new MarkerOptions().position(gasStation1).title("SPBU Pertamina"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gasStation1));

        LatLng gasStation2 = new LatLng(-7.934523,112.649551);
        mMap.addMarker(new MarkerOptions().position(gasStation2).title("SPBU Pertamina 54.651.04"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gasStation2));

        LatLng gasStation3 = new LatLng(-7.937196,112.627400);
        mMap.addMarker(new MarkerOptions().position(gasStation3).title("SPBU Pertamina 54.651.06 Soekarno Hatta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gasStation3));

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new StringBuilder(getString(R.string.nearby_url_api));
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS_METERS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + getString(R.string.google_maps_key));
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }
    }
}