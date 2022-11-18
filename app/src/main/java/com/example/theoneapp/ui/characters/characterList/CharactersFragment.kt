package com.example.theoneapp.ui.characters.characterList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theoneapp.R
import com.example.theoneapp.databinding.FragmentCharactersBinding
import com.example.theoneapp.model.Character
import com.example.theoneapp.model.CharacterResponse
import com.example.theoneapp.ui.characters.characterDescription.CharacterDescriptionActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CharactersViewModel>()
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
//        supportActionBar?.hide()
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterAdapter = CharacterAdapter()
        viewModel.retrieveCharacters()
        initObserver()
        visibleBottomNavBar()
    }

    private fun visibleBottomNavBar() {
        activity?.apply {
            findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
        }
    }

    private fun initObserver() {
        viewModel.charactersResponse.observe(viewLifecycleOwner) {
            it?.let {
                setAdapter(it)
            }
        }
        viewModel.characterError.observe(viewLifecycleOwner) {
            apiError()
        }
        viewModel.loadingStateLiveData.observe(viewLifecycleOwner) {
            it?.let {
                handleProgressBar(it)
            }
        }
    }

    private fun setSearchView() {
        searchView = binding.svSearch
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }

    private fun handleProgressBar(state: CharactersViewModel.State) {
        when (state) {
            CharactersViewModel.State.LOADING -> binding.progressBar.visibility = View.VISIBLE
            CharactersViewModel.State.LOADING_FINISHED -> binding.progressBar.visibility = View.GONE
        }
    }

    private fun apiError() {
        Toast.makeText(
            this.requireContext(),
            "Ocorreu um erro durante o carregamento",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setAdapter(character: CharacterResponse) {
        binding.rvCharacterList.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
            characterAdapter.append(character.characters)
            characterAdapter.setClickListener(object : CharacterAdapter.ClickListener {
                override fun onItemClick(characterItem: Character, position: Int) {
                    val intent = Intent(requireActivity(), CharacterDescriptionActivity::class.java)
                    intent.putExtra("characterItem", characterItem)
                    startActivity(intent)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}