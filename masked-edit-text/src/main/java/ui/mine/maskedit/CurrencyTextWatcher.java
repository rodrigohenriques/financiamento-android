package ui.mine.maskedit;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by rodrigohenriques on 7/26/15.
 */
public class CurrencyTextWatcher implements TextWatcher {

    EditText editText;

    public CurrencyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!s.toString().matches("^R$(\\d{1,3}(\\.\\d{3})*|(\\d+))(,\\d{2})?R$")) {
            String userInput = "" + s.toString().replaceAll("[^\\d]", "");
            StringBuilder cashAmountBuilder = new StringBuilder(userInput);

            while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                cashAmountBuilder.deleteCharAt(0);
            }
            while (cashAmountBuilder.length() < 3) {
                cashAmountBuilder.insert(0, '0');
            }
            cashAmountBuilder.insert(cashAmountBuilder.length() - 2, ',');

            editText.removeTextChangedListener(this);
            editText.setText(cashAmountBuilder.toString());

            editText.setTextKeepState("R$" + cashAmountBuilder.toString());
            Selection.setSelection(editText.getText(), cashAmountBuilder.toString().length() + 2);

            editText.addTextChangedListener(this);
        }
    }
}
