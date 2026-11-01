import {AuthProvider} from "./context/AuthContext.tsx";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {ProtectedRoute} from "./components/ProtectedRoute.tsx";
import {LoginPage} from "./pages/auth/LoginPage.tsx";
import {PatientListPage} from "./pages/patients/PatientListPage.tsx";
import {AppLayout} from "./components/layout/AppLayout.tsx";
import {PatientDetailPage} from "./pages/patients/PatientDetailPage.tsx";

function App() {
    return (
      <AuthProvider>
          <BrowserRouter>
              <Routes>
                  <Route path='/login' element={<LoginPage />} />

                  <Route element={<ProtectedRoute />}>
                      <Route element={<AppLayout />}>
                          <Route path='/' element={<Navigate to='patients' replace />} />
                          <Route path='/patients' element={<PatientListPage />} />
                          <Route path='/patients/:id' element={<PatientDetailPage />} />
                      </Route>
                  </Route>

                  <Route path="*" element={<Navigate to="/patients" replace />} />
              </Routes>
          </BrowserRouter>
      </AuthProvider>
    );
}

export default App
