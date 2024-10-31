
provider "azurerm" {
  features {}

  # Subscription ID for Azure subscription 1
  subscription_id = "91da8d06-e6af-41be-b465-64994ef7eeda"
  # client_id       = "your_client_id"
  # client_secret   = "your_client_secret"
  # tenant_id for Default Directory
  tenant_id       = "14959973-6dc1-48e6-90d0-4fe0b15e2cec"
}

resource "azurerm_resource_group" "neon-genaihack" {
  name     = "neon-resources"
  location = "East US 2"
}


resource "azurerm_cognitive_account" "neon-account" {
  name                = "neon-account-2"
  location            = azurerm_resource_group.neon-genaihack.location
  resource_group_name = azurerm_resource_group.neon-genaihack.name
  kind                = "OpenAI"

  sku_name = "S0"

  tags = {
    Acceptance = "Hack"
  }
}

resource "azurerm_cognitive_deployment" "neon-gpt-4" {
  name                 = "neon-gpt-4"
  cognitive_account_id = azurerm_cognitive_account.neon-account.id
  model {
    format  = "OpenAI"
    name    = "gpt-4"
    version = "turbo-2024-04-09"
  }

  sku {
    name = "GlobalStandard"
  }
}
