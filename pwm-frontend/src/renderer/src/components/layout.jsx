import React, { useState, useRef, useEffect } from 'react';
import { Lock, Plus, Minus, Settings, LogOut } from 'lucide-react';
import Swal from 'sweetalert2';
import { fetchPassword, deletePassword, addPassword, getAllPasswords } from '../../axios';

const Sidebar = ({ activeTab, setActiveTab }) => {
  const tabs = [
    { name: 'Passwords', icon: Lock },
    { name: 'Add New', icon: Plus },
  ];
  
  return (
    
    <div className="w-60 bg-gray-800 text-white h-full p-4 overflow-y-auto flex flex-col">
      <h1 className="text-xl font-bold mb-6">Password Manager</h1>
      <nav className="space-y-2 flex-grow">
        {tabs.map((tab) => (
          <button
            key={tab.name}
            className={`flex items-center w-full p-2 rounded transition-colors ${
              activeTab === tab.name ? 'bg-blue-600' : 'hover:bg-gray-700'
            }`}
            onClick={() => setActiveTab(tab.name)}
          >
            <tab.icon className="mr-2" size={18} />
            {tab.name}
          </button>
        ))}
      </nav>
    </div>
  );
};


const PasswordTable = ({ refreshTable, onRefresh }) => {
    const [passwords, setPasswords] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
  
    useEffect(() => {
      const fetchPasswords = async () => {
        try {
          const data = await getAllPasswords();
          setPasswords(data);
        } catch (err) {
          setError('Failed to fetch passwords.');
        } finally {
          setLoading(false);
        }
      };
  
      fetchPasswords();
    }, [refreshTable]); // Depend on refreshTable to refetch
  
    const handleDelete = async (website, username) => {
      if (window.confirm('Are you sure you want to delete this password?')) {
        try {
          await deletePassword(website, username);
          Swal.fire('Password deleted successfully!');
          onRefresh(); // Trigger a refresh to update the table
        } catch (error) {
          if (error.message === 'Password not found') {
            Swal.fire('Password not found. Please check the website and username.');
          } else {
            console.log(error)
            Swal.fire('Failed to delete password. Please try again later.');
          }
        }
      }
    };
  
    const handleCopy = async (website, username) => {
      try {
        const password = await fetchPassword(website, username);
        if (password !== "NOT FOUND") {
          navigator.clipboard.writeText(password);
          Swal.fire('Password copied to clipboard!');
        } else {
          Swal.fire('Password not found!');
        }
      } catch (error) {
        Swal.fire('Failed to copy password. Please try again later.');
      }
    };
    
  
    if (loading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;
  
    return (
      <div className="w-full mt-4 overflow-auto h-[500px] border rounded shadow">
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="py-2 px-4 border-b bg-gray-100 text-left">Website</th>
              <th className="py-2 px-4 border-b bg-gray-100 text-left">Username</th>
              <th className="py-2 px-4 border-b bg-gray-100 text-left">Created</th> {/* Column for actions */}
            </tr>
          </thead>
          <tbody>
            {passwords.map((password) => (
              <tr key={`${password.website}-${password.username}`}>
                <td className="py-2 px-4 border-b">{password.website}</td>
                <td className="py-2 px-4 border-b">{password.username}</td>
                <td className="py-2 px-4 border-b">{password.date} {password.time}</td>
                <td className="py-2 px-4 border-b text-right">
                  <button
                    onClick={() => handleCopy(password.website, password.username)}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 mr-2"
                  >
                    Copy
                  </button>
                  <button
                    onClick={() => handleDelete(password.website, password.username)}
                    className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  };

const AddPasswordForm = () => {
    const [website, setWebsite] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
  
    const handleSubmitadd = async (e) => {
      e.preventDefault();
      if (password !== confirmPassword) {
        setError('Passwords do not match.');
        return;
      }
  
      try {
        var add = await addPassword(website, username, password);
        if (add == "Updated!"){
            Swal.fire("Password updated!")
        }
        else{
            Swal.fire('Password added successfully!');
        }
        setWebsite('');
        setUsername('');
        setPassword('');
        setConfirmPassword('');
        setError('');
        e.preventDefault();
      } catch (err) {
        console.error('Error adding password:', err);
        setError('Failed to add password. Please try again.');
      }
    };
  
    return (
      <form onSubmit={handleSubmitadd} className="w-full p-4 border-t border-gray-300 flex flex-col items-center">
        {error && <p className="text-red-500">{error}</p>}
        <div className="flex w-full mb-4">
          <div className="flex-1 mr-2">
            <label htmlFor="website" className="block mb-1">Website:</label>
            <input
              type="text"
              id="website"
              value={website}
              onChange={(e) => setWebsite(e.target.value)}
              className="w-full p-2 border rounded"
              required
            />
          </div>
          <div className="flex-1 ml-2">
            <label htmlFor="username" className="block mb-1">Username:</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full p-2 border rounded"
              required
            />
          </div>
        </div>
        <div className="flex w-full mb-4">
          <div className="flex-1 mr-2">
            <label htmlFor="password" className="block mb-1">Password:</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-2 border rounded"
              required
            />
          </div>
          <div className="flex-1 ml-2">
            <label htmlFor="confirmPassword" className="block mb-1">Confirm Password:</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="w-full p-2 border rounded"
              required
            />
          </div>
        </div>
        <button type="submit" className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 mt-4">
          Add
        </button>
      </form>
    );
  };

  const MainContent = ({ activeTab, refreshTable, onRefresh }) => {

    const getTabContent = () => {
      switch (activeTab) {
        case 'Passwords':
          return (
            <div className="flex flex-col h-full">
              <div className="flex-grow">
                <PasswordTable key={refreshTable} onRefresh={onRefresh} />
              </div>
            </div>
          );
        case 'Add New':
          return (
            <div className="flex flex-col h-full">
              <div className="flex-grow">
                <AddPasswordForm onRefresh={onRefresh} />
              </div>
            </div>
          );
        case 'Categories':
          return "Manage your password categories.";
        default:
          return "Select a tab to view content.";
      }
    };
  
    return (
      <div className="flex-1 p-6 overflow-y-auto bg-white">
        <h2 className="text-2xl font-bold mb-4">{activeTab}</h2>
        <div>{getTabContent()}</div>
      </div>
    );
  };

  const PasswordManagerLayout = () => {
    const [activeTab, setActiveTab] = useState('Passwords');
    const [refreshTable, setRefreshTable] = useState(false);
  
    const handleRefresh = () => {
      setRefreshTable(!refreshTable);
    };
  
    return (
      <div className="flex bg-gray-100 w-full h-full" style={{ width: '1280px', height: '720px' }}>
        <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />
        <MainContent 
          activeTab={activeTab} 
          refreshTable={refreshTable} 
          onRefresh={handleRefresh} 
        />
      </div>
    );
  };

export default PasswordManagerLayout;