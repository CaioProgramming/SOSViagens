package com.intacta.sosviagens;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.intacta.sosviagens.Adapter.RecyclerAdapter;
import com.intacta.sosviagens.Adapter.RecyclerMoreThenOneAdapter;
import com.intacta.sosviagens.Adapter.RecyclerNumbersAdapter;
import com.intacta.sosviagens.Beans.Number;
import com.intacta.sosviagens.Beans.Road;
import com.intacta.sosviagens.Utils.Alerts;
import com.intacta.sosviagens.Utils.PermissionRequests;
import com.intacta.sosviagens.Utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.mateware.snacky.Snacky;

import static com.intacta.sosviagens.Utils.Utilities.Isnight;
import static com.intacta.sosviagens.Utils.Utilities.called;

public class Home extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    private Activity activity = this;
    boolean located = false; //variavel booleana para evitar que atualização de localização não pare
    Location localizacao;
    PermissionRequests requests; // Classe que verifica permissões
    RecyclerAdapter adapter; //adapter de rodovias para o recyclerview
    FusedLocationProviderClient mFusedLocationClient; //serviço de localização
    private int theme = 3;
    ArrayList<Road> roadslist = new ArrayList<>();
    ArrayList<Road> searchlist = new ArrayList<>();
    // arraylist com rodovias que será usado no recyclervi
    private GoogleMap mMap;// Classe do google maps para inicia-lo no fragment
    LocationManager manager;// gerenciador de localização
     String numero, concess; // numero e concessionária para realizar ligação quando encontrados
    BottomNavigationView navigation;
    RelativeLayout container;
    Alerts alerts;
    private static GoogleApiClient client;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    public static Road lastcall; //verifica se o usuário fez uma ligação e quando retornar ele poderá enviar comentários sobre o atendimento


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_call:
                    Load_Numbers();
                    //Método para carregar os números de emrgência
                    return true;

            }
            return false;
        }
    };

    private TextView message;
    private ProgressBar finding;
    private RecyclerView suggestions;
    private CardView concessionaries;
     private FloatingActionButton floatbutton;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.container = findViewById(R.id.container);
        this.navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        requests = new PermissionRequests(this, theme);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requests.checkLocationPermission();
        mFusedLocationClient.getLastLocation();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //showDefaultLocation();
        adapter = new RecyclerAdapter(activity, roadslist);
        adapter.notifyDataSetChanged();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);


//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        initView();
    }

    private void loadgps() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Snacky.builder().setActivity(activity).info().setText("Atualizando localização...").show();
                if (localizacao != location) {
                    actuallocation();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                actuallocation();
            }

            @Override
            public void onProviderEnabled(String s) {
                actuallocation();

            }

            @Override
            public void onProviderDisabled(String s) {
                //Snacky.builder().setActivity(activity).setText("O GPS está desativado, não foi possível obter a localização").info().show();
                //Alerts alerts = new Alerts(activity,theme);
                //alerts.buildAlertMessageNoGps();
            }
        };


        requests.checkCallPermission();
        if (requests.checkLocationPermission()) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadgps();
        actuallocation();

    }

    // Pesquisa rodovias diretamente no banco de dados
    private void Pesquisar(String pesquisa) {
        System.out.println("realizando pesquisa...");
        searchlist.clear();
        Road road = new Road();
        searchlist.add(road);
        road.setRodovia("Concessionárias registradas");
        Query databasereference;
        databasereference = FirebaseDatabase.getInstance().getReference().child("rodovias").orderByChild("rodovia")
                .startAt(pesquisa).endAt(pesquisa + "\uf8ff");


        databasereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    final Road rodovia = new Road();
                    Road r = d.getValue(Road.class);
                    System.out.println("key" + dataSnapshot.getKey());
                    if (r != null) {

                        rodovia.setConcessionaria(r.getConcessionaria());
                        rodovia.setTelefone(r.getTelefone());
                        rodovia.setId(dataSnapshot.getKey());
                        rodovia.setRodovia(r.getRodovia());
                        searchlist.add(rodovia);
                        System.out.println("Pesquisa: " + rodovia.getRodovia() + " " + rodovia.getTelefone());

                    }
                }

                GridLayoutManager llm = new GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false);
                RecyclerAdapter adapter = new RecyclerAdapter(activity, searchlist);

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro no banco de dados: " + databaseError.getMessage() + " " + databaseError.getDetails());
            }
        });


    }

    //Carrega os números salvos no servidor
    protected void Load_Numbers() {
        System.out.println("Buscando números...");
        final ArrayList<Number> numberlist = new ArrayList<>();
        numberlist.clear();
        numberlist.add(new Number(Utilities.calltitle, null, null));

        Query databasereference = FirebaseDatabase.getInstance().getReference().child("numbers").orderByChild("tipo");
        databasereference.keepSynced(true);

        databasereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Number number = new Number();
                    Number n = d.getValue(Number.class);
                    if (n != null) {
                        number.setTelefone(n.getTelefone());
                        number.setIdent(n.getIdent());
                        number.setTipo(n.getTipo());
                        numberlist.add(number);

                        System.out.println("Números de emergência: " + numberlist.size());
                    }
                }
                RecyclerNumbersAdapter numberadapter = new RecyclerNumbersAdapter(activity, numberlist);
                GridLayoutManager llm = new GridLayoutManager(activity, 1, RecyclerView.VERTICAL, false);
                numberadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro no banco de dados " + databaseError.getMessage());
            }
        });


    }

    //Caso não seja possível encontrar o usuário o mapa irá mostrar a localizção padrão
    private void showDefaultLocation() {

        mMap.setMinZoomPreference(20);
        LatLng saoPaulo = new LatLng(-23.533773, -46.625290);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(saoPaulo));
        //toolbar.setTitle(getString(R.string.default_location));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void actuallocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            requests.Location_permission();
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            address(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });

    }

    private void address(Location location) {
        //Método que retorna o nome da rua em que usário se encontra
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            System.out.println("lat:" + location.getLatitude() + "long:" + location.getLongitude());

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = addresses.get(0);
            System.out.println("Endereco " + address);
            String street = address.getThoroughfare();
            if (street == null) {
                street = address.getFeatureName();
            }

            System.out.println("Endereco atual " + street);
            if (roadslist == null || roadslist.isEmpty()) {
//                search.setQueryHint(street);
                EncontrarRodovia(street);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 14));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(15)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
            //location.setText(address);
        } catch (IOException e) {
            Snacky.builder().setActivity(activity).error().setText(e.getMessage());
        }
    }


    @Override
    protected void onRestart() {
        checkcall();
        super.onRestart();
    }

    //verifica se fez ligação para alguma concessionária e se sim exibe um alerta para o usuário comentar seu atendimento
    private void checkcall() {
        if (called) {
            Snacky.builder().setActivity(this).setText("Obrigado pela ligação").setBackgroundColor(Color.WHITE).setTextColor(Color.RED).build().show();
            Alerts alerts = new Alerts(this, theme);
            alerts.CommentAlert(lastcall);

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }

    //Método que busca a rodovia de acordo com a localização atual do usuário
    private void EncontrarRodovia(final String localizacao) {
        roadslist.clear();
        if (localizacao == null || localizacao.isEmpty()) {
            System.out.println("Nenhum endereço informado");
            return;
        }


        Query databasereference;
        databasereference = FirebaseDatabase.getInstance().getReference().child("rodovias").orderByChild("rodovia").startAt(localizacao).endAt(localizacao + "\uf8ff");

        databasereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    final Road rodovia = new Road();
                    Road r = d.getValue(Road.class);
                    if (r != null) {

                        rodovia.setConcessionaria(r.getConcessionaria());
                        rodovia.setTelefone(r.getTelefone());
                        rodovia.setId(dataSnapshot.getKey());
                        rodovia.setRodovia(r.getRodovia());
                        concess = rodovia.getConcessionaria();
                        //EncontrarComentarios(rodovia.getConcessionaria());
                        //phone.setText(rodovia.getTelefone());


                        System.out.println("esta em " + rodovia.getRodovia() + " " + rodovia.getTelefone());
                        numero = rodovia.getTelefone();
                        lastcall = rodovia;


                        System.out.println("localização atual: " + localizacao + "/n localização banco de dados: " + rodovia.getRodovia());

                    } else {
                        toolbar.setTitle(localizacao);
                    }


                    roadslist.add(rodovia);

                }
                if (dataSnapshot.getChildrenCount() == 0) {
//                    message.setText("Nenhuma concessionária localizada");
                    //finding.setVisibility(View.GONE);
                    if (roadslist.size() > 0) {
                        roadslist.clear();
                    }

                    if (alerts != null) {
                        return;
                    }
                    alerts = new Alerts(activity, theme);
                    alerts.noConcess(localizacao);
                }
                located = true;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Erro no banco de dados: " + databaseError.getMessage() + " " + databaseError.getDetails());
                toolbar.setTitle(localizacao);
            }
        });


    }


    //metodo para quando o mapa está pronto para ser carregado
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        if (!Isnight()) {
            System.out.println(Isnight());
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.

                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.day_map));

                if (!success) {
                    System.out.println("Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                System.out.println("Erro ao encontrar design " + e);
            }
        } else {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.


                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.night_map));

                if (!success) {
                    System.out.println("Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                System.out.println("Erro ao encontrar design " + e);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requests.checkLocationPermission();
            return;
        }
        mMap.setMyLocationEnabled(true);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Alerts alert = new Alerts(activity, theme);

            showDefaultLocation();


        } else {
            Toast.makeText(this, "Carregando localização atual...", Toast.LENGTH_LONG).show();
            actuallocation();
        }

        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11));// Add a marker in Sydney and move the camera

        //mMap.setMinZoomPreference(20);
        //showDefaultLocation();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        if (hasCapture) {

            //message.setText("Buscando concessionárias...");
           //finding.setVisibility(View.VISIBLE);
            actuallocation();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS turned off, Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(Home.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show dialog
                }

                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }


    private void initView() {
        message = findViewById(R.id.message);

        floatbutton = findViewById(R.id.floatbutton);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        floatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FabulousFragment dialogFrag = FabulousFragment.newInstance();
                dialogFrag.setParentFab(floatbutton);
                dialogFrag.setRoadlist(roadslist);
                dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag());
            }
        });
    }
}
