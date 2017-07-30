package ru.artempugachev.popularmovies.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import ru.artempugachev.popularmovies.R;

/**
 * Dialog for select sort order. Popular or top rated movies
 */

public class SortOrderDialog extends DialogFragment {
    public interface SortOrderDialogListener {
        void onSortOrderChange(int which);
    }

    private SortOrderDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SortOrderDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SortOrderDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_order_dialog_title)
                .setItems(R.array.sort_orders, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onSortOrderChange(which);
                    }
                });
        return builder.create();
    }
}
