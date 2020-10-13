package lk.kdu.buslocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Map;

public class NavDrawer {
    public Drawer result=null;
    public Intent intent;
    public Drawer getDrawer(final Activity activity, Bundle bundle){
        result = new DrawerBuilder()
                .withActivity(activity)
                .withSavedInstance(bundle)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home"),
                        new SecondaryDrawerItem().withName("Settings"),
                        new SecondaryDrawerItem().withName("Logout")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        Log.e("DIDI", String.valueOf(position));
                        switch (position){
                            case 0:
                                intent=new Intent(activity.getApplicationContext(), MapActivity.class);
                                break;
                            case 1:
                                intent=new Intent(activity.getApplicationContext(), Settings.class);
                                break;
                            case 2:
                                logout(activity);
                                break;
                        }
                        activity.startActivity(intent);
//                        if(position!=0){
//                            activity.finish();
//                        }
                        return false;
                    }
                }).build();
        return result;
    }

    public void logout(Activity actvity){
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("activeBuses").child();
//        GeoFire geoFire = new GeoFire(ref);
//        geoFire.removeLocation(userId);

        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(actvity.getApplicationContext(), MainActivity.class);
        actvity.startActivity(intent);
        actvity.finish();
    }
}
