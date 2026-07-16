using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.SceneManagement;

public class BukaeraScript : MonoBehaviour
{
    private void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.CompareTag("Player"))
        {
            //Debug.Log("Nibela bukatu duzu.");
            //hurrengo scenera jueteko hurrengo agindua egitearekin nahikoa litzateke zer gertatzen da azken escenara heltzen garenean?
            //SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex + 1);
            int buildIndex = SceneManager.GetActiveScene().buildIndex;
            if (buildIndex == 3)
            {
                SceneManager.LoadScene(0); // Hasierako maila kargatu
            }
            else
            {
                SceneManager.LoadScene(buildIndex + 1); // Hurrengo maila kargatu
            }

        }
    }
}
