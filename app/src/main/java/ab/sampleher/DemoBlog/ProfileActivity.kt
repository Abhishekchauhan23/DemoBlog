package ab.sampleher.DemoBlog

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth


class ProfileActivity : PostActivity() {
    val TAG="TAG"


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.menu_profile,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.menu_logout)
        {
            Log.i(TAG,"Logout")
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}


