using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.SceneManagement;

public class ScriptJokalaria : MonoBehaviour
{
    // Start is called once before the first execution of Update after the MonoBehaviour is created
    // Una vez antes del primer frame, se va ejecutar solamente cuando se de al play

    public Rigidbody rb;
    public float abiadura;//Aurrerantza juango dan abiaduraz
    public float mugitu;//Eskumatik ezkerrera mugituko dan indarraz

    int pantailaErdia;

    void Start()
    {
        //Debug.Log("Kaixo");
        //Pantailaren zabalera jasoko du eta guri erdia jakitea interesatzen zaigu
        pantailaErdia = Screen.width / 2;
    }

    // Update is called once per frame
    void Update()
    {
        //Debug.Log("Update"); 
        rb.AddForce(new Vector3(0, 0, abiadura ) * Time.deltaTime);
        //Ezkerrea mugitzeko
        if (Input.GetKey(KeyCode.A))
        {
            rb.AddForce(new Vector3(-mugitu, 0, 0) * Time.deltaTime);
        }
        //Eskumara mugitzeko
        if (Input.GetKey(KeyCode.D))
        {
            rb.AddForce(new Vector3(mugitu, 0, 0) * Time.deltaTime);
        }
        //Gure mobilean norbaitek pantaila ikutzen badu detektzeko
        if (Input.touchCount>0)
        {
            //0 jarriko dugu lehen aldiz ikutu dalako
            //Ezkerraldean ikutu du
            if (Input.GetTouch(0).position.x <= pantailaErdia)
            {
                rb.AddForce(new Vector3(-mugitu, 0, 0) * Time.deltaTime);
            }
            //Eskumaldea ikutu du
            if (Input.GetTouch(0).position.x > pantailaErdia)
            {
                rb.AddForce(new Vector3(mugitu, 0, 0) * Time.deltaTime);
            }
        }
        if (transform.position.y<-5)
        {
            SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex);
        }
    }
    private void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("Oztopo"))
        {
            //Debug.Log("Talka egin du");
            //GetActiveScene().buildIndex honekin momentuan aktibeta dugun escena lortzen dugu, momentuan gauden escena
            SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex);
            //Hurrengoa jarri geinke baie hobe da kasu hontan goikoa erabiltzea momentuan aktibeta dauen scena berriz berrarazteko
            //SceneManager.LoadScene(0);
        }
        
    }
}
