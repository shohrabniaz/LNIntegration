Get-Date | Out-File -Append D:\Integrations\LNTransfer\gts_nightly_update.log
Write-Host "Creating .lock file"
"Creating .lock file" | Out-File -Append D:\Integrations\LNTransfer\gts_nightly_update.log
New-Item -Path 'D:\Integrations\LNTransfer\nightly_update_scheduler.lock' -ItemType File
Invoke-WebRequest -Uri 'http://localhost:8443/EnoviaRestService/gts-services/nightlyUpdatesLN' | Out-File -Append D:\Integrations\LNTransfer\gts_nightly_update.log
Start-Sleep -s 15
Remove-Item -Path 'D:\Integrations\LNTransfer\nightly_update_scheduler.lock'
Get-Date | Out-File -Append D:\Integrations\LNTransfer\gts_nightly_update.log