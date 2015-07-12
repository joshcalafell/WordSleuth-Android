package com.rabbitfighter.wordsleuth.InstructionFragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.rabbitfighter.wordsleuth.R;

/**
 * Instruction dialog fragment
 *
 * @author Joshua Michael Waggoner <rabbitfighter@cryptolab.net>
 * @author Stephen Chavez <stephen.chavez12@gmail.com>
 * @version 0.2 (pre-beta)
 * @link https://github.com/rabbitfighter81/SwipeNavExample (Temporary)
 * @see 'http://developer.android.com/reference/android/database/sqlite/package-summary.html'
 * @since 0.1 2015-07-17.
 */
public class InstructionHelpDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.txt_dialog_instruction_disable)
                .setPositiveButton(R.string.txt_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        disableHelpOnStart();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void disableHelpOnStart() {
        SharedPreferences.Editor editor = getEditor();

        // sets the keypair disableHelp with false.
        editor.putBoolean(getString(R.string.app_setting_disable_help), false);
        editor.commit();

    }

    private SharedPreferences.Editor getEditor() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
        return sharedPref.edit();
    }
}
