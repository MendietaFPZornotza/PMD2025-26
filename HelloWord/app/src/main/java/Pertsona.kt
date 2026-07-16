import android.content.Context
import android.widget.Toast


class Persona // 🔹 Eraikitzailea (constructor)
    (// 🔹 Getter eta setter metodoak
    // 🔹 Atributuak (propiedades)
    var izena: String?, var adin: Int
)
{
// 🔹 Metodo gehigarri bat (adibidez)
fun agurtu(context: Context?) {
        Toast.makeText(
            context,
            "Kaixo, " + izena + "! Zure adina da " + adin + " urte.",
            Toast.LENGTH_LONG
        ).show()
}
}
