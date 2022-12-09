package devsec.app.easykitchen.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import devsec.app.easykitchen.R
import devsec.app.easykitchen.api.RestApiService
import devsec.app.easykitchen.api.RetrofitInstance
import devsec.app.easykitchen.data.models.Ingredients
import devsec.app.easykitchen.ui.main.adapter.IngredientsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class IngredientsActivity : AppCompatActivity() {
    private lateinit var adapter: IngredientsAdapter
    lateinit var recyclerView: RecyclerView
    private lateinit var ingredientsArrayList: ArrayList<String>
    private lateinit var searchArrayList: ArrayList<String>
     var igredientCart: List<String> = ArrayList()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredients)

        initIngredientsList()
        val layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.ingredientsRV)
        recyclerView.layoutManager = layoutManager
        searchArrayList = ingredientsArrayList
        adapter = IngredientsAdapter(searchArrayList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : IngredientsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = searchArrayList[position]
                Toast.makeText(this@IngredientsActivity, item, Toast.LENGTH_SHORT).show()
                igredientCart = igredientCart + item

            }
        })

        val toolbar = findViewById<Toolbar>(R.id.ingredientsToolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.ingredients_menu, menu)
        val searchItem = menu?.findItem(R.id.ingredients_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search for ingredients"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText!!.lowercase(Locale.getDefault())
                searchArrayList.clear()
                if (searchText.isNotEmpty()) {
                    ingredientsArrayList.forEach {
                        if (it.lowercase(Locale.getDefault()).contains(searchText)) {
                            searchArrayList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    searchArrayList.clear()
                    searchArrayList.addAll(ingredientsArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    private fun getIngredients(){
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getIngredientsList()
        call.enqueue(object : Callback<List<Ingredients>> {
            override fun onResponse(
                call: Call<List<Ingredients>>,
                response: Response<List<Ingredients>>
            ) {
                if (response.isSuccessful) {
                    val ingredientsList = response.body()
                    for (i in ingredientsList!!) {
                        ingredientsArrayList.add(i.name)
                        }
                    }
                adapter.notifyDataSetChanged()
                }

            override fun onFailure(call: Call<List<Ingredients>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun initIngredientsList(){
        ingredientsArrayList = ArrayList()
        searchArrayList = ArrayList()
        getIngredients()
    }
}