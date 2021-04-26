package com.example.ar_tour;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.arnavigation.Armap;
import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.route.RouteFetcher;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
import com.mapbox.turf.TurfMeta;
import com.mapbox.turf.TurfTransformation;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngBounds;
import static com.mapbox.mapboxsdk.style.expressions.Expression.within;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.mapbox.turf.TurfConstants.UNIT_KILOMETERS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {
    //maps variables....
    private MapView mapView;
    private MapboxMap mapboxmap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private DirectionsRoute directionsRoute;
    private static final String TAG = "DirectionsActivity";
    private Button startbutton;
    private NavigationMapRoute navigationMapRoute;
    public static Point destinationPoint;
    public static Point originPoint;
    private CarmenFeature home;
    private CarmenFeature work;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    // TODO:POI-LAYER PARAMETRS.......

    private static final String TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID
            = "TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID";
    private static final String TURF_CALCULATION_FILL_LAYER_ID = "TURF_CALCULATION_FILL_LAYER_ID";
    private Point lastClickPoint;
    private MapboxMap mapboxMap;
    private String circleUnit = UNIT_KILOMETERS;
    private int circleRadius = 4 / 2;
    private boolean visionManagerWasInit = false;
    private boolean navigationWasStarted = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MapboxNavigation mapboxNavigation;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RouteFetcher routeFetcher;
    private RouteProgress lastRouteProgress;
    //private LocationEngine locationEngine;
    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //view to create instances of map........
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getContext().getApplicationContext(), getString(R.string.mapbox_access_token));
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(MapsFragment.this);
        return view;
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxmap = mapboxMap;
        mapboxmap.setStyle(new Style.Builder().fromUri(Style.DARK)
                .withSource(new GeoJsonSource(TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID)), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enablelocationcomponent(style);
                addDestinationLayer(style);
                initSearchFab();
                addUserLocations();
                // Create an empty GeoJSON source using the empty feature collection
                setUpSource(style);

                // Set up a new symbol layer for displaying the searched location's feature coordinates
                setupLayer(style);
                /*reverseGeocodeFunc(Point.fromLngLat(originPoint.longitude(),originPoint.latitude()));*/
                //poi within layer........
                hideLayers();

                initPolygonCircleFillLayer();
                drawPolygonCircle(Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                        locationComponent.getLastKnownLocation().getLatitude()));
                mapboxmap.addOnMapClickListener(MapsFragment.this);
                startbutton = (getActivity()).findViewById(R.id.startButton);
                startbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean simulateroute = false;
                        NavigationLauncherOptions Options = NavigationLauncherOptions.builder().directionsRoute(directionsRoute)
                                .shouldSimulateRoute(simulateroute).build();
                        NavigationLauncher.startNavigation(getActivity(), Options);

                    }
                });

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button explorebtn = (view).findViewById(R.id.explore);
        explorebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UnityPlayerActivity.class);
                startActivity(intent);
            }
        });

        Button Arbutton = (view).findViewById(R.id.Armode);
        Arbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Armap.class);

                    startActivity(intent);
            }
        });

    }


    private void addDestinationLayer(@NonNull Style LoadedMapStyle) {
        LoadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        LoadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        LoadedMapStyle.addLayer(destinationSymbolLayer);

    }

    @SuppressWarnings({"MissingPermission"})
    private void enablelocationcomponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {

            // Get an instance of the component
            locationComponent = mapboxmap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
           /* Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);*/
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            //startVisionManager();
            //startNavigation();
            mapboxmap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enablelocationcomponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            //finish();
        }

    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxmap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }
        //poi layer code.....
        mapboxmap.easeCamera(CameraUpdateFactory.newLatLng(point));
        //lastClickPoint = Point.fromLngLat(point.getLongitude(),point.getLatitude());
        lastClickPoint = originPoint;
        drawPolygonCircle(lastClickPoint);

        getRoute(originPoint, destinationPoint);
        startbutton.setEnabled(true);
        startbutton.setBackgroundResource(R.color.mapbox_blue);
        return true;

    }

    private void getRoute(Point Originpoint, Point destinationpoint) {
        NavigationRoute.builder(getContext()).accessToken(Mapbox.getAccessToken()).origin(Originpoint).destination(destinationpoint).build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;

                        }
                        directionsRoute = response.body().routes().get(0);
                        //drawing routes on map.....
                        if (navigationMapRoute != null) {
                            navigationMapRoute.updateRouteArrowVisibilityTo(false);
                            navigationMapRoute.updateRouteArrowVisibilityTo(false);

                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxmap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(directionsRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error");

                    }
                });

    }

    private void initSearchFab() {
        getView().findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder().accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                        .placeOptions(PlaceOptions.builder().backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .addInjectedFeature(home)
                                .addInjectedFeature(work)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }

    private void addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
                .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
                .placeName("50 Beale St, San Francisco, CA")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Mapbox DC Office")
                .placeName("740 15th Street NW, Washington DC")
                .geometry(Point.fromLngLat(-77.0338348, 38.899750))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxmap != null) {
                Style style = mapboxmap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    mapboxmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                    ((Point) selectedCarmenFeature.geometry()).longitude()))
                            .zoom(14)
                            .build()), 4000);
                }
            }
        }


    }


    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }

    /**
     * Remove other types of label layers from the style in order to highlight the POI label layer.
     */
    private void hideLayers() {
        mapboxmap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                SymbolLayer roadLabelLayer = style.getLayerAs("road-label");
                if (roadLabelLayer != null) {
                    roadLabelLayer.setProperties(visibility(Property.NONE));
                }
                SymbolLayer transitLabelLayer = style.getLayerAs("transit-label");
                if (transitLabelLayer != null) {
                    transitLabelLayer.setProperties(visibility(Property.NONE));
                }
                SymbolLayer roadNumberShieldLayer = style.getLayerAs("road-number-shield");
                if (roadNumberShieldLayer != null) {
                    roadNumberShieldLayer.setProperties(visibility(Property.NONE));
                }

            }
        });
    }


    private void drawPolygonCircle(Point circleCenter) {
        mapboxmap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Use Turf to calculate the Polygon's coordinates
                Polygon polygonArea = getTurfPolygon(circleCenter, circleRadius, circleUnit);

                List<Point> pointList = TurfMeta.coordAll(polygonArea, false);

                // Update the source's GeoJSON to draw a new circle
                GeoJsonSource polygonCircleSource = style.getSourceAs(TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID);
                if (polygonCircleSource != null) {
                    polygonCircleSource.setGeoJson(Polygon.fromOuterInner(
                            LineString.fromLngLats(pointList)));
                }

                // Show new places of interest (POIs)
                filterPlacesOfInterest(polygonArea);

                // Adjust camera bounds to include entire circle
                List<LatLng> latLngList = new ArrayList<>(pointList.size());
                for (Point singlePoint : pointList) {
                    latLngList.add(new LatLng((singlePoint.latitude()), singlePoint.longitude()));
                }
                mapboxmap.easeCamera(newLatLngBounds(
                        new LatLngBounds.Builder()
                                .includes(latLngList)
                                .build(), 75), 1000);
            }
        });

    }

    private void filterPlacesOfInterest(Polygon polygonArea) {
        mapboxmap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                SymbolLayer poiLabelLayer = style.getLayerAs("poi-label");
                if (poiLabelLayer != null) {
                    poiLabelLayer.setFilter(within(polygonArea));
                }
            }
        });
    }

    private Polygon getTurfPolygon(@NonNull Point centerPoint, double radius,
                                   @NonNull String units) {
        return TurfTransformation.circle(centerPoint, radius / 10, 360, units);
    }

    private void initPolygonCircleFillLayer() {
        mapboxmap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Create and style a FillLayer based on information that will come from the Turf calculation
                FillLayer fillLayer = new FillLayer(TURF_CALCULATION_FILL_LAYER_ID,
                        TURF_CALCULATION_FILL_LAYER_GEOJSON_SOURCE_ID);
                fillLayer.setProperties(
                        fillColor(Color.parseColor("#f5425d")),
                        fillOpacity(.5f));
                style.addLayerBelow(fillLayer, "poi-label");
            }
        });
    }

    //handling lifecycle of android activity........
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        //startVisionManager();
        //startNavigation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}

