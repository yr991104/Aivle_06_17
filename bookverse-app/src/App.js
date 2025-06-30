import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './pages/MainPage';
import AuthorManagement from './pages/AuthorManagement';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/author" element={<AuthorManagement />} />
      </Routes>
    </Router>
  );
}

export default App;
