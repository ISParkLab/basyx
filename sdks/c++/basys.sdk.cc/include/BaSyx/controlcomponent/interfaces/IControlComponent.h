#ifndef BASYX_CONTROLCOMPONENT_ICONTROLCOMPONENT_H
#define BASYX_CONTROLCOMPONENT_ICONTROLCOMPONENT_H

#include <string>

#include <BaSyx/shared/object.h>

#include <BaSyx/controlcomponent/interfaces/IStatusMap.h>
#include <BaSyx/controlcomponent/interfaces/IControlComponentChangeListener.h>

#include <BaSyx/controlcomponent/enumerations/OccupationState.h>
#include <BaSyx/controlcomponent/enumerations/ExecutionMode.h>

/**
 * BaSys 4.0 control component interface. This is a VAB object that cannot be serialized.
 */
namespace basyx {
namespace controlcomponent {

class ControlComponent
{
public:
  ControlComponent();

  /**
   * Add ControlComponentChangeListener
   */
  virtual void addControlComponentChangeListener(const IControlComponentChangeListener &listener) = 0;

  /**
   * Remove ControlComponentChangeListener
   */
  virtual void removeControlComponentChangeListener(const IControlComponentChangeListener &listener) = 0;

  /**
   * Get "operations" map
   */
  const basyx::object &getServiceOperationMap();

  /**
   * Update an value
   */
  virtual void put(const std::string &key, const basyx::object &value) = 0;

  /**
   * Finish current execution state (execute 'SC' order). This only works in transition states
   */
  virtual void finishState() = 0;

  /**
   * Get order list
   */
  virtual basyx::object getOrderList() = 0;

  /**
   * Add order to order list
   */
  virtual void addOrder(const std::string &newOrder) = 0;

  /**
   * Clear order list
   */
  virtual void clearOrder() = 0;

  /**
   * Get occupation state
   */
  virtual OccupationState getOccupationState() = 0;

  /**
   * Set occupation state
   */
  virtual void setOccupationState(const OccupationState &occSt) = 0;

  /**
   * Get occupier ID
   */
  virtual std::string getOccupierID() = 0;;

  /**
   * Set occupier ID
   */
  virtual void setOccupierID(const std::string &occId) = 0;

  /**
   * Get last occupier ID
   */
  virtual std::string getLastOccupierID() = 0;;

  /**
   * Set last occupier ID
   */
  virtual void setLastOccupierID(const std::string &occId) = 0;

  /**
   * Get execution mode
   */
  virtual ExecutionMode getExecutionMode() = 0;

  /**
   * Set execution mode
   */
  virtual void setExecutionMode(const ExecutionMode &exMode) = 0;

  /**
   * Get execution state
   */
  virtual std::string getExecutionState() = 0;;

  /**
   * Set execution state
   */
  virtual void setExecutionState(const std::string &newSt) = 0;

  /**
   * Get operation mode
   */
  virtual std::string getOperationMode() = 0;;

  /**
   * Set operation mode
   */
  virtual void setOperationMode(const std::string &opMode) = 0;

  /**
   * Get work state
   */
  virtual std::string getWorkState() = 0;;

  /**
   * Set work state
   */
  virtual void setWorkState(const std::string &workState) = 0;

  /**
   * Get error state
   */
  virtual std::string getErrorState() = 0;;

  /**
   * Set error state
   */
  virtual void setErrorState(const std::string &errorState) = 0;

  /**
   * Get last error state
   */
  virtual std::string getLastErrorState() = 0;;

  /**
   * Set last error state
   */
  virtual void setLastErrorState(const std::string &lastErrorState) = 0;

  /**
   * Get last command
   */
  virtual std::string getCommand() = 0;;

  /**
   * Set command
   */
  virtual void setCommand(const std::string &cmd) = 0;

  /**
   * Get local overwrite variable
   */
  virtual std::string getLocalOverwrite() = 0;;

  /**
   * Set local overwrite variable
   */
  virtual void setLocalOverwrite(const std::string &cmd) = 0;

  /**
   * Get local overwrite free variable
   */
  virtual std::string getLocalOverwriteFree() = 0;;

  /**
   * Set local overwrite free variable
   */
  virtual void setLocalOverwriteFree(const std::string &cmd) = 0;

};

}
}

#endif /* BASYX_CONTROLCOMPOMPONENT_ICONTROLCOMPONENT_H */