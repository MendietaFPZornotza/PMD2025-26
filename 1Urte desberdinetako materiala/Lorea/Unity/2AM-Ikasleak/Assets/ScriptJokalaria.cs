using UnityEngine;

public class ScriptJokalaria : MonoBehaviour
{
    // Start is called once before the first execution of Update after the MonoBehaviour is created
   
    public Rigidbody rb;
    public float Abiadura;

    void Start()
    {
        Debug.Log("Kaixo");
    }

    // Update is called once per frame
    void Update()
    {
        //Debug.Log("Update");
        rb.AddForce(new Vector3(-Abiadura, 0,0) * Time.deltaTime);
       
    }
}
