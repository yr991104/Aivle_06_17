// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './pages/MainPage';
import AuthorManagement from './pages/AuthorManagement';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import AISystem from './pages/AISystem';
import LibraryPlatform from './pages/LibraryPlatform';
import AdminSystem from './pages/AdminSystem'; // ✅ 추가됨
import SubscriberSystem from './pages/SubscriberSystem';
import PointSystem from './pages/PointSystem';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/author" element={<AuthorManagement />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/aisystem" element={<AISystem />} />
        <Route path="/library" element={<LibraryPlatform />} />
        <Route path="/admin" element={<AdminSystem />} /> {/* ✅ 관리자 시스템 라우팅 */}
        <Route path="/subscriber" element={<SubscriberSystem />} />
        <Route path="/points" element={<PointSystem />} />
      </Routes>
    </Router>
  );
}

export default App;
