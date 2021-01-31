package ab.sampleher.DemoBlog

import ab.sampleher.DemoBlog.models.Posts
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter (val context: Context,val posts:List<Posts>) :RecyclerView.Adapter<PostAdapter.Viewholder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
       val view= LayoutInflater.from(context).inflate(R.layout.item_post,parent,false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
      return posts.size
    }

    inner class Viewholder(itemview: View): RecyclerView.ViewHolder(itemview){
       // var tvUsername: TextView=itemview.findViewById(R.id.tvUsername)
        fun bind(posts: Posts)
        {
           itemView.findViewById<TextView>(R.id.tvUsername).text=posts.user?.username
            itemView.findViewById<TextView>(R.id.tvDescription).text=posts.description
            Glide.with(context).load(posts.imageUrl).into(itemView.findViewById(R.id.ivPost))
            itemView.findViewById<TextView>(R.id.tvRelativeTime).text=DateUtils.getRelativeTimeSpanString(posts.creationTimeMs)
        }
    }

}