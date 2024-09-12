import axios from 'axios';

export const fetchPassword = async (website, username) => {
    try {
      const response = await axios.get('http://localhost:8080/api/password', {
        params: {
          website: website,
          username: username,
        },
      });
  
      return response.data;  // Return the fetched password
    } catch (error) {
      console.error('Error fetching password:', error);
      throw error; // Rethrow the error for handling in the calling component
    }
  };

  export const deletePassword = async (website, username) => {
    try {
      const response = await axios.delete('http://localhost:8080/api/password', {
        params: {
          website: website,
          username: username,
        },
      });
  
      return response.data;  // Return the response data if deletion is successful
    } catch (error) {
      if (error.response && error.response.status === 404) {
        // Handle the 404 error specifically
        console.error('Password not found:', error.response.data);
        throw new Error('Password not found'); // You can customize this message as needed
      } else {
        // Handle other errors
        console.error('Error deleting password:', error);
        throw new Error('Failed to delete password'); // You can customize this message as needed
      }
    }
  };

  export const addPassword = async (website, username, password) => {
    try {
      const response = await axios.post('http://localhost:8080/api/password', null, {
        params: {
          website: website,
          username: username,
          password: password,
        },
      });
  
      // Return the server's response
      return response.data;
    } catch (error) {
      // Handle and log any errors
      console.error('Error adding password:', error);
  
      // If a specific error response is received, like a 404 or 500, throw it for further handling
      throw error;
    }
  };

  // Function to get all passwords
export const getAllPasswords = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/passwords'); // Adjust the URL as per your backend configuration
      return response.data;  // The response data contains the JSON object with all passwords
    } catch (error) {
      console.error('Error fetching all passwords:', error);
      throw error; // Rethrow the error for handling in the calling component
    }
  };