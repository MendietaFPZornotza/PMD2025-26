using UnityEngine;
using UnityEngine.SceneManagement;

public class menuManager : MonoBehaviour
{
    public void playButton()
    {
        SceneManager.LoadScene(1);
    }
}
