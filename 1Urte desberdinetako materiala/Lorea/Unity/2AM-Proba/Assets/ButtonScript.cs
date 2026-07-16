using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
 

public class ButtonScript : MonoBehaviour
{
    // Start is called before the first frame update
    public Sprite b1;
    public Sprite b2;
    public Sprite b3;
    public Sprite b4;
    public Button buttonChange;
    public int buttonValue = 1;
    void Start()
    {

        buttonChange.image.sprite = b1;

    }

    // Update is called once per frame
    public void ChangeImage()
    {

        buttonValue++;

        switch (buttonValue)
        {
            case 1:
                buttonChange.image.sprite = b2;
                buttonValue = 1;
                break;

            case 2:
                buttonChange.image.sprite = b3;
                buttonValue = 2;
                break;

            case 3:
                buttonChange.image.sprite = b4;
                buttonValue = 0;
                break;

            default:
                Debug.Log("There is a error, please verify the code");
                break;
        }

    }
}
