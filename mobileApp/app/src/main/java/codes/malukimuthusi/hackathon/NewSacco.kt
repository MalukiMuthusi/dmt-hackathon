package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import codes.malukimuthusi.hackathon.viewModels.NewSaccoFragmentViewModel
import codes.malukimuthusi.hackathon.viewModels.NewSaccoFragmentViewModel.Companion.doSomethingWhenChecked

class NewSacco : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_sacco)
    }

    fun onCheckboxClicked(view: View) {
        val checked = (view as CheckBox).isChecked
        when (view.id) {
            R.id.fiveToSeven -> {
                doSomethingWhenChecked(
                    checked,
                    "fiveToSeven",
                    NewSaccoFragmentViewModel.pickHoursList
                )
            }
            R.id.sevenToNine -> {
                doSomethingWhenChecked(
                    checked,
                    "sevenToNine",
                    NewSaccoFragmentViewModel.pickHoursList
                )

            }
            R.id.nineTo11 -> {
                doSomethingWhenChecked(checked, "nineTo11", NewSaccoFragmentViewModel.pickHoursList)
            }
            R.id.elevenTo2 -> {
                doSomethingWhenChecked(
                    checked,
                    "elevenTo2",
                    NewSaccoFragmentViewModel.pickHoursList
                )
            }
            R.id.twoTo4 -> {
                doSomethingWhenChecked(checked, "twoTo4", NewSaccoFragmentViewModel.pickHoursList)
            }
            R.id.fourToSix -> {
                doSomethingWhenChecked(
                    checked,
                    "fourToSix",
                    NewSaccoFragmentViewModel.pickHoursList
                )
            }
            R.id.sixTo9 -> {
                doSomethingWhenChecked(checked, "sixTo9", NewSaccoFragmentViewModel.pickHoursList)
            }
            R.id.nine12 -> {
                doSomethingWhenChecked(checked, "nine12", NewSaccoFragmentViewModel.pickHoursList)
            }
        }
    }
}
