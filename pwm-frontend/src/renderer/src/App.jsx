import Versions from './components/Versions'
import electronLogo from './assets/electron.svg'
import PasswordManagerLayout from './components/layout'

function App() {
  const ipcHandle = () => window.electron.ipcRenderer.send('ping')

  return (<PasswordManagerLayout></PasswordManagerLayout>)
}


export default App

