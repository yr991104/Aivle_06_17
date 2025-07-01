// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './pages/MainPage';
import AuthorManagement from './pages/AuthorManagement';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import AISystem from './pages/AISystem'; // ✅ 추가

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/author" element={<AuthorManagement />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/aisystem" element={<AISystem />} /> 
      </Routes>
    </Router>
  );
}

export default App;
